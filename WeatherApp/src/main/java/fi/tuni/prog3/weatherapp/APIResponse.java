/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import com.google.gson.*;
import java.util.ArrayList;

/**
 *
 * @author kush1
 */
public class APIResponse implements iAPI {


    private String API_KEY ="98bcec7e722ae03228134634e61add8c";

    /**
     * Returns the coordinates for a location in the format "City,Country" returns null if the location is invalid
     * @param loc location name in format "City,Country"
     * @return String
     */
    @Override
    public String lookUpLocation(String loc) {
        try {
            String[] parts = loc.split(",");
            String city = parts[0].trim();
            String country = parts[1].trim();
            String lon = "", lat = "", coordinates = "";        
            String URLString = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s", city, country, API_KEY);

        
            StringBuilder result = new StringBuilder();
            URL url = new URL(URLString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line=reader.readLine())!=null){
                result.append(line);
            }
            
            JsonObject resultJson = JsonParser.parseString(result.toString()).getAsJsonObject();
            String jsonCountry = resultJson.getAsJsonObject("sys").get("country").getAsString();
            
            if (jsonCountry.equalsIgnoreCase(country)) {
                lon = resultJson.getAsJsonObject("coord").get("lon").toString();
                lat = resultJson.getAsJsonObject("coord").get("lat").toString();
                coordinates = String.format("%s,%s", lat, lon);
                return coordinates;
            }

            return coordinates;

        } catch (IOException e) {

        }
        catch (JsonSyntaxException e){

        }
        catch (ArrayIndexOutOfBoundsException ex){
            
        }
        return null;
    }

    /**
    Returns the weather as a string joined by , if the coordinates lat and lon are invalid then null string is returned
    * @param lat latitude of given location
    * @param lon longitude of given location
    * return String 
    */
    @Override
    public String getCurrentWeather(double lat, double lon) {
        Double latDouble = Double.valueOf(lat);
        Double lonDouble = Double.valueOf(lon);
        String URLString  = "https://api.openweathermap.org/data/2.5/weather?lat=" + Double.toString(latDouble) + "&lon=" + Double.toString(lonDouble)+"&appid=" + API_KEY;

        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(URLString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line=reader.readLine())!=null){
                result.append(line);
            }

            
            JsonObject resultJson = JsonParser.parseString(result.toString()).getAsJsonObject();
            JsonArray weatherArray = resultJson.getAsJsonArray("weather");
            JsonObject weatherObject = weatherArray.get(0).getAsJsonObject();


            String ambient = weatherObject.get("main").getAsString();
            String icon = weatherObject.get("icon").getAsString();
            String description = weatherObject.get("description").getAsString();
            String temperature = resultJson.getAsJsonObject("main").get("temp").getAsString();
            long dtValue = resultJson.get("dt").getAsLong();
            String dt = String.valueOf(dtValue);
            String tempFeelsLike = resultJson.getAsJsonObject("main").get("feels_like").getAsString();
            String tempMin = resultJson.getAsJsonObject("main").get("temp_min").getAsString();
            String tempMax = resultJson.getAsJsonObject("main").get("temp_max").getAsString();
            String pressure = resultJson.getAsJsonObject("main").get("pressure").getAsString();
            String humidity = resultJson.getAsJsonObject("main").get("humidity").getAsString();
            String windSpeed = resultJson.getAsJsonObject("wind").get("speed").getAsString();
            String windDeg = resultJson.getAsJsonObject("wind").get("deg").getAsString();
            JsonObject sysObject = resultJson.getAsJsonObject("sys");
            String sunrise = sysObject.get("sunrise").getAsString();
            String sunset = sysObject.get("sunset").getAsString();
            
            String weather = String.join(", ", ambient,icon, description, temperature, tempFeelsLike, tempMin, tempMax, pressure, humidity, windSpeed, windDeg,dt,sunrise,sunset);
            
            return weather;
            
        } catch (MalformedURLException ex) {
            
        } catch (IOException ex) {
            
        } 
        
        return null;
    }

    /**
    Returns the daily forecast as a Json string if the coordinates lat and lon are invalid then null string is returned
    * @param lat latitude of given location
    * @param lon longitude of given location
    * return String 
    */
    @Override
    public String getForecast(double lat, double lon,Character unit) {

        String tempType = "";
        if (unit.equals('F')){
            tempType = "imperial";
        }
        if (unit.equals('C')){
            tempType = "metric";
        }
        if (unit.equals('K')){
            tempType = "standard";
        }
        Double latDouble = Double.valueOf(lat);
        Double lonDouble = Double.valueOf(lon);
        String URLString  = "https://pro.openweathermap.org/data/2.5/forecast/hourly?lat=" + Double.toString(latDouble) + "&lon=" + Double.toString(lonDouble)+"&units="+tempType+"&appid=" + API_KEY;

        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(URLString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line=reader.readLine())!=null){
                result.append(line);
            }
            
            

            return result.toString();
            
        } catch (MalformedURLException ex) {
            
        } catch (IOException ex) {
            
        }
        return null;
          
    }

    /**
    Returns the daily forecast as a Json string if the coordinates lat and lon are invalid then null string is returned
    * @param lat latitude of given location
    * @param lon longitude of given location
    * return String 
    */
    public String getDailyForecast(double lat,double lon,int days,Character unit){
        String tempType = "";
        if (unit.equals('F')){
            tempType = "imperial";
        }
        if (unit.equals('C')){
            tempType = "metric";
        }
        if (unit.equals('K')){
            tempType = "standard";
        }
        Double latDouble = Double.valueOf(lat);
        Double lonDouble = Double.valueOf(lon);
        String URLString  = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + Double.toString(latDouble) + "&lon=" + Double.toString(lonDouble)+
            "&cnt=" + days + "&units="+tempType+ "&appid=" + API_KEY; 
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(URLString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line=reader.readLine())!=null){
                result.append(line);
            }
            return result.toString();
            
        } catch (MalformedURLException ex) {
            
        } catch (IOException ex) {
            
        }
        return null;
    }

    /**
    Returns the available countries /locations given a city as an arraylist if city is invalid then 
    * @param lat latitude of given location
    * @param lon longitude of given location
    * return ArrayList String 
    */
    public ArrayList<String> getAvailableCountries(String city) throws IOException{    
        String URLString = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=10&appid=%s", city, API_KEY);
        ArrayList<String> places = new ArrayList<>();

        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(URLString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line=reader.readLine())!=null){
                result.append(line);
            }

            JsonArray jsonArray = new JsonParser().parse(result.toString()).getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
            String country = jsonElement.getAsJsonObject().get("country").getAsString();
            String place = jsonElement.getAsJsonObject().get("name").getAsString();
            String whole_place = String.format("%s,%s,%s",place,city,country);
            places.add(whole_place);
        }

        }
        catch (IOException ex){
            return null;
        }
        
        return places;
    }   
}
