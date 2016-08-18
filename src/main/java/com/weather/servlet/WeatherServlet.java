package com.weather.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;


/**
 * Created by Saniye on 09.08.16.
 */
public class WeatherServlet extends HttpServlet {
//    String htmlFormatPage = "<html>\n" +
//            "<head>\n" +
//            "    <title>Weather for today</title>\n" +
//            "</head>\n" +
//            "<body>\n" +
//            "<form method=\"post\"><select multiple=\"multiple\" size=\"10\" name=\"select-country\">\n" +
//            "        <option value=\"2643743\">London</option>\n" +
//            "        <option value=\"5059248\">Tokio</option>\n" +
//            "        <option value=\"2950159\">Berlin</option>\n" +
//            "        <option value=\"2988507\">Paris</option>\n" +
//            "        <option value=\"696050\">Kyiv</option>\n" +
//            "        <option value=\"1248991\">Colombo</option>\n" +
//            "        <option value=\"2553604\">Casablanka</option>\n" +
//            "        <option value=\"6619707\">Mumbai</option>\n" +
//            "        <option value=\"2530335\">Tangier</option>\n" +
//            "    </select>\n" +
//            "<input type=\"submit\" value=\"Submit\" name=\"submit\">\n" +
//            "</form>\n" +
//            "<p>\n" +
//            " <form method=\"post\"> <input type=\"number\" size=\"10\" value=\"\" name=\"latitude\">\n" +
//            " <input type=\"number\" size=\"10\" value=\"\" name=\"longitude\">\n" +
//            "<input type=\"submit\" value=\"Submit\" name=\"submit_coordinates\">\n" +
//            "</form>\n" +
//            "</body>\n" +
//            "</html>";

//    String errorText="<html>\n" +
//            " <head>\n"+
//            " <title>Weather for today</title>\n"+
//            " </head>\n" +
//            " <body>\n" +
//            "<p>Error: Not found city</p>\n"+
//            "</body>\n"+
//            " </html>";

    private static final Logger LOG = Logger.getLogger(WeatherServlet.class);
    private OpenWeatherAPIConnector openWeatherAPIConnector = new OpenWeatherAPIConnector();
    WeatherDBConnector dbConnector = new WeatherDBConnector();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] countries = req.getParameterValues("select-country");
        try {
            dbConnector.createDbUserTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Weather> resultList = new ArrayList<Weather>();
        if (countries == null)
        {   try{ resultList.add(workWithLocation(req.getParameter("latitude"), req.getParameter("longitude")));}
           catch (WeatherNotFoundException e){
               RequestDispatcher dispatcher = req.getRequestDispatcher("/error.jsp");
               if (dispatcher != null) {
                   dispatcher.forward(req, resp);
               }
//               resp.getOutputStream().print(errorText);
               return;}}
        else resultList.addAll(workWithCityIDs(countries));

//
//        printResult(resp, resultList);

        req.setAttribute("resultList", resultList);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/res.jsp");
        dispatcher.forward(req, resp);
    }


    private void printResult(HttpServletResponse resp, List<Weather> resultList) throws IOException {
        String header = String.format("%-10s%-15s%-30s%-15s%-15s%-15s%-15s%-15s%n",
                "Country", "City", "General information", "Temeperature", "Wind speed", "Source","Latitude","Longitude");
        resp.getOutputStream().print(header);
        for (Weather w : resultList) {
            String s = String.format("%-10s%-25s%-25s%-15.1f%-15.1f%-15s%-15.1f%-15.1f%n", w.country, w.name, w.main, w.temp, w.windSpeed, w.source,w.latitude,w.longitude);
            resp.getOutputStream().print(s);
        }
    }

    private Weather workWithLocation(String lat, String lon) throws IOException, WeatherNotFoundException {
        Weather w = dbConnector.selectFromDBByLocationAndDate(lat, lon);
        if (w == null) {
            URL apiRequest = openWeatherAPIConnector.createRequestByLocation(lat, lon);
            LOG.trace("ApiWeather: request - " + apiRequest.toString());
            HttpURLConnection http = (HttpURLConnection) apiRequest.openConnection();
            int responseCode = http.getResponseCode();
            LOG.debug("\nSending 'GET' request to URL : " + apiRequest);
            LOG.debug("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(http.getInputStream()));
            String inputLine;

            StringBuffer weatherAPIresponse = new StringBuffer();
            if(responseCode==404){throw new WeatherNotFoundException();}
            while ((inputLine = in.readLine()) != null) {
                weatherAPIresponse.append(inputLine);
            }
            in.close();
            return openWeatherAPIConnector.parseJsonRespondByLocation(weatherAPIresponse.toString());
        } else return w;
    }

    private List<Weather> workWithCityIDs(String[] countries) throws IOException {
        Map<String, Weather> exist = dbConnector.getWeatherInfoFromDbByCityId(countries);
        List<Weather> resultList=new ArrayList<Weather>();
        Set<String> countrySet = exist.keySet();
        List<String> cityList = new ArrayList<String>(Arrays.asList(countries));
        cityList.removeAll(countrySet);
        if(cityList.size()!=0) {
        URL apiRequest = openWeatherAPIConnector.createRequestByIDs(cityList);
        LOG.trace("ApiWeather: request - " + apiRequest.toString());
        HttpURLConnection http = (HttpURLConnection) apiRequest.openConnection();

        int responseCode = http.getResponseCode();
        LOG.debug("\nSending 'GET' request to URL : " + apiRequest);
        LOG.debug("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(http.getInputStream()));
        String inputLine;
        StringBuffer weatherAPIresponse = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            weatherAPIresponse.append(inputLine);
        }
        in.close();
       resultList = openWeatherAPIConnector.parseJsoneResponceByIDs(weatherAPIresponse.toString());}
        for (Weather w : exist.values()) {
            if (w.name != null) resultList.add(w);
        }
        return resultList;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("text/html");
//        resp.getOutputStream().print(htmlFormatPage);

//        getServletContext().getRequestDispatcher("/index.jsp");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/index.jsp");

        if (dispatcher != null) {

            dispatcher.forward(req, resp);

        }
    }


}




