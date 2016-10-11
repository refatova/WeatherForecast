package com.weather.servlet;

/**
 * Created by Saniye on 12.08.16.
 */
public class Weather {
    String country;
    String name;
    String main;

    double temp;
    double windSpeed;
    int id;

    String source;
    double latitude;
    double longitude;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        if (Double.compare(weather.temp, temp) != 0) return false;
        if (Double.compare(weather.windSpeed, windSpeed) != 0) return false;
        if (id != weather.id) return false;
        if (Double.compare(weather.latitude, latitude) != 0) return false;
        if (Double.compare(weather.longitude, longitude) != 0) return false;
        if (country != null ? !country.equals(weather.country) : weather.country != null) return false;
        if (name != null ? !name.equals(weather.name) : weather.name != null) return false;
        if (main != null ? !main.equals(weather.main) : weather.main != null) return false;
        return !(source != null ? !source.equals(weather.source) : weather.source != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        result = country != null ? country.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (main != null ? main.hashCode() : 0);
        temp1 = Double.doubleToLongBits(temp);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(windSpeed);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        result = 31 * result + id;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        temp1 = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        return result;
    }
}
