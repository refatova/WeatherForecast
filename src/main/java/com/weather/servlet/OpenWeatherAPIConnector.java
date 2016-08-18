package com.weather.servlet;

import com.google.common.base.Joiner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saniye on 12.08.16.
 */
public class OpenWeatherAPIConnector {

    public static final String API_WEATHER = "http://api.openweathermap.org/data/2.5/group?APPID=ca4ffb526c9aad62c910a66578fcc91c";
    WeatherDBConnector dbConnector = new WeatherDBConnector();

    //api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}
    protected URL createRequestByIDs(List counties) throws MalformedURLException {
        return new URL(API_WEATHER + "&id=" + Joiner.on(",").join(counties));
    }

    protected URL createRequestByLocation(String lat, String lon) throws MalformedURLException {
        return new URL("http://api.openweathermap.org/data/2.5/weather?APPID=ca4ffb526c9aad62c910a66578fcc91c&lat=" + lat + "&lon=" + lon);
    }


    List<Weather> parseJsoneResponceByIDs(String str) {

        List<Weather> forecast = new ArrayList<Weather>();

        JsonElement rootElement = new JsonParser().parse(str);
        JsonArray weatherList = rootElement.getAsJsonObject().get("list").getAsJsonArray();
        for (JsonElement je : weatherList
                ) {
            Weather w1 = new Weather();
            w1.id = je.getAsJsonObject().get("id").getAsInt();
            w1.name = je.getAsJsonObject().get("name").getAsString();
            w1.country = je.getAsJsonObject().get("sys").getAsJsonObject().get("country").getAsString();
            w1.main = je.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString();
            w1.windSpeed = je.getAsJsonObject().get("wind").getAsJsonObject().get("speed").getAsDouble();
            w1.temp = je.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsDouble();
            w1.longitude = je.getAsJsonObject().get("coord").getAsJsonObject().get("lon").getAsDouble();
            w1.latitude = je.getAsJsonObject().get("coord").getAsJsonObject().get("lat").getAsDouble();
            w1.source = "API";
            forecast.add(w1);
            dbConnector.insertIntoDB(w1);
        }

        return forecast;
    }

    protected Weather parseJsonRespondByLocation(String str) throws WeatherNotFoundException {
//        JsonElement rootElement=new JsonParser().parse()
        JsonElement rootElement = new JsonParser().parse(str);
        if (rootElement.getAsJsonObject().get("cod") != null) {
            if (rootElement.getAsJsonObject().get("cod").getAsString().equals("404")) {throw new WeatherNotFoundException();}
        }
        Weather w = new Weather();
        w.name = rootElement.getAsJsonObject().get("name").getAsString();
        w.country = rootElement.getAsJsonObject().get("sys").getAsJsonObject().get("country").getAsString();
        w.main = rootElement.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString();
        w.id = rootElement.getAsJsonObject().get("id").getAsInt();
        w.temp = rootElement.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsDouble();
        w.windSpeed = rootElement.getAsJsonObject().get("wind").getAsJsonObject().get("speed").getAsDouble();
        w.longitude = rootElement.getAsJsonObject().get("coord").getAsJsonObject().get("lon").getAsDouble();
        w.latitude = rootElement.getAsJsonObject().get("coord").getAsJsonObject().get("lat").getAsDouble();
        w.source = "API";
        dbConnector.insertIntoDB(w);
        return w;
    }

}
