package fi.tuni.prog3.weatherapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Weather class for handling weather data
 * @author Daniil
 */
public class Weather {

    private JsonObject currentWeather;
    private List<Map<String, String>> weatherHourly;
    private List<Map<String, String>> weatherDaily;
    
    /* Unit conversion */
    private final Double ca = 1.0;
    private final Double cb = 273.25;
    private final Double cs = 1.0;

    private final Double fa = 1.8;
    private final Double fb = 459.67;
    private final Double fs = 2.2369362921;
    
    private Double aCurr;
    private Double bCurr;
    private Double sCurr;
    private String unitStr;

    /**
     * retrieves the current weather data for the specified city and country
     * @param city The city for which to get the weather
     * @param country The coutry code for the specified city
     * @return JsonObject containing the current weather data
     */
    public JsonObject getWeather(String city, String country) {
        String coordinates = new APIResponse().lookUpLocation(String.format("%s,%s", city, country));
        if (coordinates == null){
            return null;
        }
        String[] parts = coordinates.replaceAll(" ", "").split(",");
 
        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);

        String currentW = new APIResponse().getCurrentWeather(lat, lon);

        this.currentWeather = parseWeatherString(currentW);
        
        return parseWeatherString(currentW);
    }

    /**
     * Parses the raw weather data string into a JsonObject
     * @param weather Raw weather data string
     * @return JsonObject conaining parsed weather data
     */
    private JsonObject parseWeatherString(String weather){
        String[] parts = weather.split(", ");

        JsonObject weatherJson = new JsonObject();

        String[] parameters = {"ambient", 
                               "icon",
                               "description", 
                               "temp", 
                               "tempFL", 
                               "tempMin", 
                               "tempMax", 
                               "pressure", 
                               "humidity", 
                               "windSpeed", 
                               "windDeg", 
                               "dt", 
                               "sunrise", 
                               "sunset"};

        for (int p = 0; p < parameters.length; p++){
            String name = parameters[p];
            String part = parts[p];
            weatherJson.addProperty(name, part);
        }

        return weatherJson;
    }

    /**
     * Gets the formatted sunrise time
     * @return Formatted surise time
     */
    public String getSunrise(){
        String sunrise = this.currentWeather.get("sunrise").toString().replace("\"", "");
        long sunriseVal = Long.parseLong(sunrise);
        Instant instant = Instant.ofEpochSecond(sunriseVal);
        ZonedDateTime utc = instant.atZone(ZoneId.of("UTC"));
        String formatted = utc.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        return formatted;
    }

    /**
     * Gets the formatted sunset time
     * @return Formatted sunset time
     */
    public String getSunset(){
        String sunset = this.currentWeather.get("sunset").toString().replace("\"", "");
        long sunsetValue = Long.parseLong(sunset);
        Instant instant = Instant.ofEpochSecond(sunsetValue);
        ZonedDateTime utc = instant.atZone(ZoneId.of("UTC"));
        String formatted = utc.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        return formatted;
    }

    /**
     * Gets the weather icon code
     * @return Weather icon code
     */
    public String getIcon(){
        String icon = this.currentWeather.get("icon").toString();
        icon = icon.substring(1, icon.length() - 1);
        return icon;
    }

    /**
     * Gets the formatted time.
     * @return Formatted time
     */
    public String getTime(){
        String dt = this.currentWeather.get("dt").toString().replace("\"", "");
        long dtValue = Long.parseLong(dt);
        Instant instant = Instant.ofEpochSecond(dtValue);
        ZonedDateTime utc = instant.atZone(ZoneId.of("UTC"));
        String formatted = utc.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        return formatted;
    }

    /**
     * Gets the ambient weather condition
     * @return Ambient weather condition
     */
    public String getAmbient(){
        String ambient = this.currentWeather.get("ambient").toString();
        return ambient.substring(1, ambient.length() - 1);
    }

    /**
     * Gets the weather description
     * @return Weather description
     */
    public String getDesc(){
        String desc = this.currentWeather.get("description").toString();
        return desc.substring(1, desc.length() - 1);
    }

    /**
     * Gets the current temperature with the specified uni
     * @param unit Temperature unit
     * @return Current temperature with the specifid unit
     */
    public String getTemp(Character unit){
        if(unit.equals('C')) {
            this.aCurr = this.ca;
            this.bCurr = this.cb;
            this.unitStr = " °C";
        }
        else if(unit.equals('F')) {
            this.aCurr = this.fa;
            this.bCurr = this.fb;
            this.unitStr = " °F";
        }
        
        String temp = this.currentWeather.get("temp").toString();
        temp = temp.substring(1, temp.length() - 1);
        temp = String.format("%.1f", 
                             Double.parseDouble(temp) * this.aCurr - this.bCurr);
        temp = temp + this.unitStr;

        return temp;
    }

    /**
     * Gets the "feels like" temperature with the specified unit
     * @param unit Temperature unit ('C' for Celsius, 'F' for Fahrenheit)
     * @return "Feels like" temperature with the specified unit
     */
    public String getTempFL(Character unit){
        if(unit.equals('C')) {
            this.aCurr = this.ca;
            this.bCurr = this.cb;
            this.unitStr = " °C";
        }
        else if(unit.equals('F')) {
            this.aCurr = this.fa;
            this.bCurr = this.fb;
            this.unitStr = " °F";
        }

        String tempFL = this.currentWeather.get("tempFL").toString();
        tempFL = tempFL.substring(1, tempFL.length() - 1);
        tempFL = String.format("%.1f", 
                               Double.parseDouble(tempFL) * this.aCurr - this.bCurr);
        tempFL = tempFL + this.unitStr;

        return tempFL;  
    }

    /**
     * Gets the minimum temperature with the specified unit
     * @param unit Temperature unit
     * @return Minimum temperature with the specified unit
     */
    public String getTempMin(Character unit){

        if(unit.equals('C')) {
            this.aCurr = this.ca;
            this.bCurr = this.cb;
            this.unitStr = " °C";
        }
        else if(unit.equals('F')) {
            this.aCurr = this.fa;
            this.bCurr = this.fb;
            this.unitStr = " °F";
        }
        String tempMin = this.currentWeather.get("tempMin").toString();
        tempMin = tempMin.substring(1, tempMin.length() - 1);
        tempMin = String.format("%.1f", 
                               Double.parseDouble(tempMin) * this.aCurr - this.bCurr);
        tempMin = tempMin + this.unitStr;

        return tempMin;        
    }

    /**
     * Gets the maximum temperature with the specified unit
     * @param unit Temperature unit 
     * @return Maximum temperature with the specified unit
     */
    public String getTempMax(Character unit){

        if(unit.equals('C')) {
            this.aCurr = this.ca;
            this.bCurr = this.cb;
            this.unitStr = " °C";
        }
        else if(unit.equals('F')) {
            this.aCurr = this.fa;
            this.bCurr = this.fb;
            this.unitStr = " °F";
        }

        String tempMax = this.currentWeather.get("tempMax").toString();
        tempMax = tempMax.substring(1, tempMax.length() - 1);
        tempMax = String.format("%.1f", 
                               Double.parseDouble(tempMax) * this.aCurr - this.bCurr);
        tempMax = tempMax + this.unitStr;

        return tempMax;         
    }

    /**
     * Gets the atmospheric pressure
     * @return Atmospheric pressure
     */
    public String getPressure(){
        String pressure = this.currentWeather.get("pressure").toString();
        return pressure.substring(1, pressure.length() - 1);         
    }

    /**
     * Gets the humidity
     * @return Humidity
     */
    public String getHumidity(){
        String humidity = this.currentWeather.get("humidity").toString();
        
        return humidity.substring(1, humidity.length() - 1);        
    }

    /**
     * Gets the wind speed with the specified unit
     * @param unit Wind speed unit 
     * @return Wind speed with the specified unit
     */
    public String getWindSpeed(Character unit){
        if(unit.equals('C')) {
            this.sCurr = this.cs;
            this.unitStr = " m/s";
        }
        else if(unit.equals('F')) {
            this.sCurr = this.fs;
            this.unitStr = " mph";
        }
        String windSpeed = this.currentWeather.get("windSpeed").toString();
        windSpeed = windSpeed.substring(1, windSpeed.length() - 1);
        windSpeed = String.format("%.1f", 
                               Double.parseDouble(windSpeed) * this.sCurr);
        windSpeed = windSpeed + this.unitStr;
        return windSpeed; 
    }

    /**
     * Gets the wind direction arrow based on the wind degree
     * @return Wind direction
     */
    public String getWindDeg(){
        String windDeg = this.currentWeather.get("windDeg").toString();
        Integer windDegInt = Integer.parseInt(windDeg.substring(1, windDeg.length() - 1));
        String windDir;
        
        if(0 <= windDegInt && windDegInt < 23) {
            windDir = " →";
        }
        else if(windDegInt < 68) {
            windDir = " ↗";
        }
        else if(windDegInt < 113) {
            windDir = " ↑";
        }
        else if(windDegInt < 158) {
            windDir = " ↖";
        }
        else if(windDegInt < 203) {
            windDir = " ←";
        }
        else if(windDegInt < 248) {
            windDir = " ↙";
        }
        else if(windDegInt < 293) {
            windDir = " ↓";
        }
        else if(windDegInt < 338) {
            windDir = " ↘";
        }
        else {
            windDir = " →";
        }
        
        return windDir;      
    }

    /**
     * Retrieves the hourly weather forecast for the specified city and country
     * @param city The city for which to get the forecast
     * @param country The country code for the specified city
     * @param unit Temperature unit
     * @return List of hourly weather data
     */
    public  List<Map<String, String>> getWeatherHourly(String city, String country, Character unit) {
        String coordinates = new APIResponse().lookUpLocation(String.format("%s,%s", city, country));
        if (coordinates == null) {
            return null;  // Return an empty list
        }
        String[] parts = coordinates.replaceAll(" ", "").split(",");

        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);

        String weatherH = new APIResponse().getForecast(lat, lon,unit);

        this.weatherHourly = parseWeatherHourly(weatherH);
        
        return this.weatherHourly/*parseWeatherHourly(weatherH)*/;
    }

    /**
     * Parses raw hourly weather data string into a list of maps
     * @param weather Raw hourly weather data
     * @return List of hourly weather data
     */
    private  List<Map<String, String>> parseWeatherHourly(String weather){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(weather, JsonObject.class);

        JsonArray listArray = jsonObject.getAsJsonArray("list");
        List<Map<String, String>> resultList = new ArrayList<>();

        for (int i = 0; i < Math.min(24, listArray.size()); i++) {
            JsonObject listItem = listArray.get(i).getAsJsonObject();

            JsonObject mainObject = listItem.getAsJsonObject("main");
            JsonArray weatherArray = listItem.getAsJsonArray("weather");
            JsonObject weatherObject = weatherArray.get(0).getAsJsonObject();
            String desc = listItem.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

            String icon = listItem.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
            Map<String, String> resultMap = new HashMap<>();



            String dt = listItem.get("dt").getAsString();
            long dtValue = Long.parseLong(dt);
            Instant instantDt = Instant.ofEpochSecond(dtValue);
            ZonedDateTime utcDt = instantDt.atZone(ZoneId.of("UTC"));
            String formattedDt;
            formattedDt = utcDt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            resultMap.put("dt_txt", formattedDt);

            
            String tempString = mainObject.get("temp").getAsString();
            
            tempString = String.format("%.1f", Double.valueOf(tempString));
            
            resultMap.put("temp", tempString);
            
            resultMap.put("weather_main", weatherObject.get("main").getAsString());
            resultMap.put("desc", desc);
            resultMap.put("icon",icon);
            resultList.add(resultMap);
        }

        return resultList;
    }

    /**
     * Retrieves the daily weather forecast for the specified ciy and country
     * @param city The city for which to get the forecast
     * @param country The country code for the specified city
     * @param unit Temperature unit
     * @return List of daiy weather data 
     */
    public List<Map<String, String>> getWeatherDaily(String city, String country,Character unit){
        String coordinates = new APIResponse().lookUpLocation(String.format("%s,%s", city, country));
        if (coordinates == null){
            return null;
        }
        String[] parts = coordinates.replaceAll(" ", "").split(",");

        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);

        String weatherD = new APIResponse().getDailyForecast(lat, lon, 5, unit);
            
        return parseWeatherDaily(weatherD);
    }

    /**
     * Parses the raw daily weather data string into a list of maps
     * @param weather daily weather data 
     * @return List of daily weather data 
     */
    private List<Map<String, String>> parseWeatherDaily(String weather){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(weather, JsonObject.class);

        JsonArray forecastList = jsonObject.getAsJsonArray("list");
        List<Map<String, String>> resultList = new ArrayList<>();

        for (JsonElement element : forecastList) {
            JsonObject listItem = element.getAsJsonObject();

            String dt = listItem.get("dt").getAsString();
            long dtValue = Long.parseLong(dt);
            Instant instantDt = Instant.ofEpochSecond(dtValue);
            ZonedDateTime utcDt = instantDt.atZone(ZoneId.of("UTC"));
            String formattedDt;
            formattedDt = utcDt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            
            String sunrise = listItem.get("sunrise").getAsString();
            long sunriseValue = Long.parseLong(sunrise);
            Instant instantSunrise = Instant.ofEpochSecond(sunriseValue);
            ZonedDateTime utcSunrise = instantSunrise.atZone(ZoneId.of("UTC"));
            String formattedSunrise = utcSunrise.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            
            String sunset = listItem.get("sunset").getAsString();
            long sunsetValue = Long.parseLong(sunset);
            Instant instantSunset = Instant.ofEpochSecond(sunsetValue);
            ZonedDateTime utcSunset = instantSunset.atZone(ZoneId.of("UTC"));
            
            String formattedSunset = utcSunset.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            
            String tempDay = String.format("%.1f", listItem.getAsJsonObject("temp").get("day").getAsDouble());
            String tempMax = String.format("%.1f", listItem.getAsJsonObject("temp").get("max").getAsDouble());
            String tempMin = String.format("%.1f", listItem.getAsJsonObject("temp").get("min").getAsDouble());
            
            String ambient = listItem.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
            String desc = listItem.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
            String icon = listItem.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

            Map<String, String> dayInfo = new HashMap<>();

            dayInfo.put("sunrise", formattedSunrise);
            dayInfo.put("sunset", formattedSunset);
            dayInfo.put("icon", icon);
            dayInfo.put("date", formattedDt);
            dayInfo.put("temp_day", tempDay);
            dayInfo.put("temp_max", tempMax);
            dayInfo.put("temp_min", tempMin);
            dayInfo.put("ambient", ambient);
            dayInfo.put("desc", desc);

            resultList.add(dayInfo);
        }
        
        this.weatherDaily = resultList;

        return resultList;
    }

    /**
     * Gets the list of hourly weather
     * @return List of hourly weather data
     */
    public List<Map<String, String>> getHourlyWeather(){
        return this.weatherHourly;
    }

    /**
     * Gets the list of daily weather data
     * @return List of daily weather data
     */
    public List<Map<String, String>> getDailyWeather(){
        return this.weatherDaily;
    }
}
