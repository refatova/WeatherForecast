package com.weather.servlet;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saniye on 09.08.16.
 */
public class WeatherServletTest {

    @org.testng.annotations.Test
    public void testDoGet() throws Exception {

    }

    @Test
    public void checkRequestUrl() throws Exception {
        WeatherServlet ws = new WeatherServlet();
        OpenWeatherAPIConnector openWeatherAPIConnector = new OpenWeatherAPIConnector();
        List l = new ArrayList();
        l.add("2643743");
        l.add("2950159");
        URL urlik = openWeatherAPIConnector.createRequestByIDs(l);
        Assert.assertEquals(urlik.toString(), "http://api.openweathermap.org/data/2.5/group?APPID=ca4ffb526c9aad62c910a66578fcc91c&id=2643743,2950159");
    }

//    @Test
//    public void checkParsingJson() {
//        final String response = "{\"cnt\":2,\"list\":[{\"coord\":{\"lon\":-98.99,\"lat\":47.98},\"sys\":{\"type\":3,\"id\":9683,\"message\":0.3367,\"country\":\"US\",\"sunrise\":1470914832,\"sunset\":1470966884},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"main\":{\"temp\":294.82,\"humidity\":76,\"pressure\":1010,\"temp_min\":294.82,\"temp_max\":294.82},\"wind\":{\"speed\":3.13,\"deg\":297.001},\"rain\":{\"3h\":0},\"clouds\":{\"all\":36},\"dt\":1470938277,\"id\":5059248,\"name\":\"Fort Totten\"},{\"coord\":{\"lon\":13.41,\"lat\":52.52},\"sys\":{\"type\":3,\"id\":135165,\"message\":0.0357,\"country\":\"DE\",\"sunrise\":1470887037,\"sunset\":1470940732},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"main\":{\"temp\":287.95,\"humidity\":59,\"pressure\":1019,\"temp_min\":286.48,\"temp_max\":289.82},\"wind\":{\"speed\":5.28,\"deg\":268.001},\"clouds\":{\"all\":44},\"dt\":1470938267,\"id\":2950159,\"name\":\"Berlin\"}]}";
//        OpenWeatherAPIConnector openWeatherAPIConnector = new OpenWeatherAPIConnector();
//        List<Weather> lw = openWeatherAPIConnector.parseJsoneResponceByIDs(response);
//        Assert.assertTrue(!lw.isEmpty());
//    }

//    @Test
//    public void checkSelectToDb() {
//        WeatherDBConnector con = new WeatherDBConnector();
//        Assert.assertTrue(con.getWeatherInfoFromDbByCityId(new String[]{"264374300"}).isEmpty());
//    }

//    @Test
//    public void checkInsertRowToDB() {
//        Weather w = new Weather();
//        w.temp = 232.2;
//        w.windSpeed = 3.4;
//        w.main = "Funny";
//        w.country = "Laos";
//        w.id = 3232323;
//        w.name = "Laosita-DeMar";
//
//        WeatherDBConnector con = new WeatherDBConnector();
//        con.insertIntoDB(w);
//    }
}