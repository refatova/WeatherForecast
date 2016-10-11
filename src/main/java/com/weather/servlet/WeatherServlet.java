package com.weather.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;


/**
 * Created by Saniye on 09.08.16.
 */

@WebServlet("/")
public class WeatherServlet extends HttpServlet {


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
        ArrayList<Weather> resultList = new ArrayList<Weather>();
        if (countries == null) {
            try {
                resultList.add(workWithLocation(req.getParameter("latitude"), req.getParameter("longitude")));
            } catch (WeatherNotFoundException e) {
                RequestDispatcher dispatcher = req.getRequestDispatcher("/error.jsp");
                if (dispatcher != null) {
                    dispatcher.forward(req, resp);
                }
                return;
            }
        } else resultList.addAll(workWithCityIDs(countries));

        req.setAttribute("list", resultList);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/res.jsp");
        dispatcher.forward(req, resp);
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
            if (responseCode == 404) {
                throw new WeatherNotFoundException();
            }
            while ((inputLine = in.readLine()) != null) {
                weatherAPIresponse.append(inputLine);
            }
            in.close();
            return openWeatherAPIConnector.parseJsonRespondByLocation(weatherAPIresponse.toString());
        } else return w;
    }

    private List<Weather> workWithCityIDs(String[] countries) throws IOException {
        Map<String, Weather> exist = dbConnector.getWeatherInfoFromDbByCityId(countries);
        List<Weather> resultList = new ArrayList<Weather>();
        Set<String> countrySet = exist.keySet();
        List<String> cityList = new ArrayList<String>(Arrays.asList(countries));
        cityList.removeAll(countrySet);
        if (cityList.size() != 0) {
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
            resultList = openWeatherAPIConnector.parseJsoneResponceByIDs(weatherAPIresponse.toString());
        }
        for (Weather w : exist.values()) {
            if (w.name != null) resultList.add(w);
        }
        return resultList;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
   if (true) throw new IOException();
        RequestDispatcher dispatcher = req.getRequestDispatcher("/index.jsp");
        if (dispatcher != null) {
            dispatcher.forward(req, resp);
        }
    }


}




