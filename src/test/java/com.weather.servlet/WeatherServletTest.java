package com.weather.servlet;

import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

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

    @Test
    public void checkParsingJson() {
        final String response = "{\"cnt\":2,\"list\":[{\"coord\":{\"lon\":-98.99,\"lat\":47.98},\"sys\":{\"type\":3,\"id\":9683,\"message\":0.3367,\"country\":\"US\",\"sunrise\":1470914832,\"sunset\":1470966884},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"main\":{\"temp\":294.82,\"humidity\":76,\"pressure\":1010,\"temp_min\":294.82,\"temp_max\":294.82},\"wind\":{\"speed\":3.13,\"deg\":297.001},\"rain\":{\"3h\":0},\"clouds\":{\"all\":36},\"dt\":1470938277,\"id\":5059248,\"name\":\"Fort Totten\"},{\"coord\":{\"lon\":13.41,\"lat\":52.52},\"sys\":{\"type\":3,\"id\":135165,\"message\":0.0357,\"country\":\"DE\",\"sunrise\":1470887037,\"sunset\":1470940732},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"main\":{\"temp\":287.95,\"humidity\":59,\"pressure\":1019,\"temp_min\":286.48,\"temp_max\":289.82},\"wind\":{\"speed\":5.28,\"deg\":268.001},\"clouds\":{\"all\":44},\"dt\":1470938267,\"id\":2950159,\"name\":\"Berlin\"}]}";
        OpenWeatherAPIConnector openWeatherAPIConnector = new OpenWeatherAPIConnector();
        List<Weather> lw = openWeatherAPIConnector.parseJsoneResponceByIDs(response);
        Assert.assertTrue(!lw.isEmpty());
    }


    @Test
    public void checkSelectToDb() {
        WeatherDBConnector spyDB= Mockito.spy(new WeatherDBConnector());
        doReturn(null).when(spyDB).selectFromDbByIdAndDate(696050);
        Assert.assertTrue(spyDB.getWeatherInfoFromDbByCityId(new String[]{"696050"}).isEmpty());
    }

    @Test
    public void checkSelectToDb1() {
        WeatherDBConnector spyDB= Mockito.spy(new WeatherDBConnector());
        Weather w=new Weather();
        w.name="Mockito";
        w.country="Mockito";
        w.source="Mockito";
        w.id=01;
        w.windSpeed=1.3;
        w.longitude=1.3;
        w.latitude=1.3;

        Weather w1=new Weather();
        w1.name="Mockito1";
        w1.country="Mockito1";
        w1.source="Mockito1";
        w1.id=02;
        w1.windSpeed=1.3;
        w1.longitude=1.3;
        w1.latitude=1.3;

        doReturn(w).when(spyDB).selectFromDbByIdAndDate(696050);
        doReturn(w1).when(spyDB).selectFromDbByIdAndDate(2);
        doReturn(null).when(spyDB).selectFromDbByIdAndDate(3);
        Map<String, Weather> excistCitiesInDB= spyDB.getWeatherInfoFromDbByCityId(new String[]{"696050","2","3"});
        Assert.assertTrue( excistCitiesInDB.get("696050").equals(w));
        Assert.assertTrue(excistCitiesInDB.get("2").equals(w1));
        Assert.assertTrue(excistCitiesInDB.size()==2);
    }

    @Test
    public void checkInsertRowToDB() {
        Weather w = new Weather();
        w.temp = 232.2;
        w.windSpeed = 3.4;
        w.main = "Funny";
        w.country = "Laos";
        w.id = 3232323;
        w.name = "Laosita-DeMar";

        WeatherDBConnector con = new WeatherDBConnector();
        con.insertIntoDB(w);
    }
}