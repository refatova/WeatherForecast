package com.weather.servlet;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Created by Saniye on 12.08.16.
 */
public class WeatherDBConnector {
    private static final Logger LOG = Logger.getLogger(WeatherDBConnector.class);
    //    private static SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");
    //  DateTimeFormatter dateFormat=DateTimeFormatter(DateTimeFormatter.ISO_LOCAL_DATE);
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //    LocalDate date = LocalDate.now();
//    String text = LocalDate.now().format(formatter);
    static LocalDate parsedDate = LocalDate.parse(LocalDate.now().format(formatter), formatter);
    Date date = Date.valueOf(parsedDate);

    //    static Connection dbConnection;
    private static final String DB_NAME = "Weather";


    public Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Context ctx = new InitialContext(); // JNDI
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/weatherdb"); // JNDI
            dbConnection = ds.getConnection(); // JNDI

//            DriverManager.registerDriver(new org.mariadb.jdbc.Driver());
//            dbConnection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/", "root", "");
//            String useDB = "use Weather;";
//            dbConnection.createStatement().execute(useDB);

        } catch (SQLException e) {
            LOG.error(e.getMessage());
        } catch (NamingException e) {
           LOG.error(e.getMessage());
        }
        return dbConnection;
    }

    protected void createDbUserTable() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String createTableSQL = "CREATE TABLE IF NOT EXISTS `Forecast` (\n" +
                "  `city_ID` varchar(100) NOT NULL,\n" +
                "  `city` varchar(100) NOT NULL,\n" +
                "  `country` varchar(100) NOT NULL,\n" +
                "  `date` date NOT NULL,\n" +
                "  `main_info` varchar(100) NOT NULL,\n" +
                "  `temperature` float NOT NULL,\n" +
                "  `wind_speed` float NOT NULL,\n" +
                "  `source` varchar(100) NOT NULL,\n" +
                "  `latitude` float NOT NULL,\n" +
                "  `longitude` float NOT NULL,\n" +
                "  PRIMARY KEY (`city_ID`,`date`)\n" +
                ")";

        //   String insertToWeatherTable="insert into Weather.Forecast (city,city_id, temperature,wind_speed, main_info,date) values (%s,%d,%f,%f,%s,%s)";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }


    private Weather selectFromDbByIdAndDate(int id) {
        String selectFromForecastTableSQL = "select * from Weather.Forecast where city_ID =? and date=?";
        PreparedStatement prepareStatement = null;
        Connection dbConnection = null;
        Weather w = null;
        try {
            dbConnection = getDBConnection();
            dbConnection.setAutoCommit(false);
            prepareStatement = dbConnection.prepareStatement(selectFromForecastTableSQL);
            prepareStatement.setInt(1, id);
            prepareStatement.setDate(2, date);

            ResultSet result = prepareStatement.executeQuery();
            LOG.trace("RESULTSET after executing the query" + result);


            if (result.next()) {
                w = createWeatherObjectFromResultSet(result);
//                w = new Weather();
//                w.name = result.getString("city");
//                w.id = result.getInt("city_id");
//                w.country = result.getString("country");
//                w.main = result.getString("main_info");
//                w.temp = result.getDouble("temperature");
//                w.windSpeed = result.getDouble("wind_speed");
//                w.latitude = result.getDouble("latitude");
//                w.longitude = result.getDouble("longitude");
//                w.source = "DataBase";
            }

            dbConnection.commit();
//
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (prepareStatement != null) {
                try {
                    prepareStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return w;

    }

    protected Map<String, Weather> getWeatherInfoFromDbByCityId(String[] countries) {
        Map<String, Weather> excistCitiesInDB = new HashMap<String, Weather>();
//        List<String> notExistInDB = new ArrayList<String>();
        for (int i = 0; i < countries.length; i++) {
            Weather w = selectFromDbByIdAndDate(Integer.parseInt(countries[i]));
            if (w != null) excistCitiesInDB.put(countries[i], w);
//            else notExistInDB.add(countries[i]);
        }

        return excistCitiesInDB;
    }


    protected Weather selectFromDBByLocationAndDate(String lat, String lon) {
        String selectFromForecastTableSQL = "select * from Weather.Forecast where latitude =? and longitude=? and date=?";
        PreparedStatement prepareStatement = null;
        Connection dbConnection = null;
        Weather w = null;
        try {
            dbConnection = getDBConnection();
            dbConnection.setAutoCommit(false);
            prepareStatement = dbConnection.prepareStatement(selectFromForecastTableSQL);
            prepareStatement.setDouble(1, Double.parseDouble(lat));
            prepareStatement.setDouble(2, Double.parseDouble(lon));
            prepareStatement.setDate(3, date);

            ResultSet result = prepareStatement.executeQuery();
            LOG.trace("RESULTSET after executing the query" + result);


            if (result.next()) w = createWeatherObjectFromResultSet(result);
            dbConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (prepareStatement != null) {
                try {
                    prepareStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return w;
    }

    private Weather createWeatherObjectFromResultSet(ResultSet result) throws SQLException {
        Weather w = new Weather();
//        w = new Weather();
        w.name = result.getString("city");
        w.id = result.getInt("city_id");
        w.country = result.getString("country");
        w.main = result.getString("main_info");
        w.temp = result.getDouble("temperature");
        w.windSpeed = result.getDouble("wind_speed");
        w.latitude = result.getDouble("latitude");
        w.longitude = result.getDouble("longitude");
        w.source = "DataBase";
        return w;
    }

    protected void insertIntoDB(Weather w) {
        PreparedStatement insertWeather = null;
        Connection dbConnection = null;
        String insertIntoForecastTableString = "insert into Weather.Forecast(city,city_id, temperature,wind_speed, main_info,date,country,source,latitude,longitude) values (?,?,?,?,?,?,?,?,?,?)";

        try {
            dbConnection = getDBConnection();
            dbConnection.setAutoCommit(false);
            insertWeather = dbConnection.prepareStatement(insertIntoForecastTableString);
            insertWeather.setString(1, w.name);
            insertWeather.setInt(2, w.id);
            insertWeather.setDouble(3, w.temp);
            insertWeather.setDouble(4, w.windSpeed);
            insertWeather.setString(5, w.main);
            insertWeather.setDate(6, date);
            insertWeather.setString(7, w.country);
            insertWeather.setString(8, w.source);
            insertWeather.setDouble(9, w.latitude);
            insertWeather.setDouble(10, w.longitude);
            insertWeather.executeUpdate();

            dbConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (insertWeather != null) {
                try {
                    insertWeather.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
