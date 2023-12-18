/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import fi.tuni.prog3.weatherapp.Weather;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 *
 * @author kush1
 */
public class WeatherTest {
    @Test
    void testGetWeather() {
        // Create a Weather object
        Weather weather = new Weather();
        String correctCity = "Tampere";
        String correctCountry = "fi";
        // Call the method we want to test
        JsonObject result = weather.getWeather(correctCity, correctCountry);

        // Check that the method returned a result
        assertNotNull(result);

        // Check that the result has the expected fields
        assertTrue(result.has("ambient"));
        assertTrue(result.has("description"));
        assertTrue(result.has("temp"));
        assertTrue(result.has("tempFL"));
        assertTrue(result.has("tempMin"));
        assertTrue(result.has("tempMax"));
        assertTrue(result.has("pressure"));
        assertTrue(result.has("humidity"));
        assertTrue(result.has("windSpeed"));
        assertTrue(result.has("windDeg"));
        assertTrue(result.has("dt"));
        // ...add more assertions for the other fields...
        String incorrectCity = "tamper";
        String incorrectCountry = "D";
        Weather weather_exception = new Weather();
        assertNull( weather_exception.getWeather(incorrectCity, correctCountry));

        assertNull(weather_exception.getWeather(correctCity, incorrectCountry));
        
        assertNull(weather_exception.getWeather(incorrectCity, incorrectCountry));
    
    }

    @Test
    void testGetAmbient() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getAmbient());
    }

    @Test
    void testGetDesc() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getDesc());
    }

    @Test
    void testGetTemp() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getTemp('C'));
    }

    @Test
    void testGetTempFL() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getTempFL('F'));
    }

    @Test
    void testGetTempMin() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getTempMin('F'));
    }

    @Test
    void testGetTempMax() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getTempMax('C'));
    }

    @Test
    void testGetPressure() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getPressure());
    }

    @Test
    void testGetHumidity() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getHumidity());
    }

    @Test
    void testGetWindSpeed() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getWindSpeed('C'));
    }

    @Test
    void testGetWindDeg() {
        Weather weather = new Weather();
        weather.getWeather("Tampere", "fi");
        assertNotNull(weather.getWindDeg());
    }
    @Test
    public void testGetWeatherHourly() {
        // Arrange
        Weather weather = new Weather();
        String correctCity = "Tampere";
        String correctCountry = "fi";
        String incorrectCity = "tamper";
        String incorrectCountry = "D";
        // Act
        List<Map<String, String>> result = weather.getWeatherHourly("New York", "us",'F');
         for (Map<String, String> map : result) {
            assertTrue(map.containsKey("dt_txt"));
            assertTrue(map.containsKey("temp"));
            assertTrue(map.containsKey("weather_main"));
            assertTrue(map.containsKey("desc"));
        }
        // Assert
        assertFalse(result.isEmpty());
        // Add more assertions here based on your expected result
        Weather weather_exception = new Weather();
        assertNull(weather_exception.getWeatherHourly(incorrectCity, correctCountry,'F'));
                
        assertNull(weather_exception.getWeatherHourly(correctCity, incorrectCountry,'C'));

        assertNull(weather_exception.getWeatherHourly(incorrectCity, incorrectCountry,'F'));
    }

    @Test
    public void testGetWeatherDaily() {

        Weather weather = new Weather();
        String correctCity = "Tampere";
        String correctCountry = "fi";
        String incorrectCity = "tamper";
        String incorrectCountry = "D";

        List<Map<String, String>> result = weather.getWeatherDaily("New York", "us",'F');
        for (Map<String, String> map : result) {
            assertTrue(map.containsKey("date"));
            assertTrue(map.containsKey("temp_day"));
            assertTrue(map.containsKey("temp_max"));
            assertTrue(map.containsKey("temp_min"));
            assertTrue(map.containsKey("ambient"));
            assertTrue(map.containsKey("desc"));
        }
        assertFalse(result.isEmpty());
        // Add more assertions here based on your expected result
        Weather weather_exception = new Weather();
        assertNull(weather_exception.getWeatherDaily(incorrectCity, correctCountry,'F'));
     
        assertNull(weather_exception.getWeatherDaily(correctCity, incorrectCountry,'F'));

        assertNull(weather_exception.getWeatherDaily(incorrectCity, incorrectCountry,'C'));

    }
}
