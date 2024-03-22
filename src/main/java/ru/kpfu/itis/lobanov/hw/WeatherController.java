package ru.kpfu.itis.lobanov.hw;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.lobanov.hw.exceptions.WeatherApiConnectionException;
import ru.kpfu.itis.lobanov.hw.httpclient.HttpClient;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final HttpClient httpClient;
    public static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=143c9d8999112b2f489a1e3a44de6ade";
    public static final String MAIN_KEY = "main";
    public static final String TEMP_KEY = "temp";
    public static final String HUMIDITY_KEY = "humidity";
    public static final String WEATHER_KEY = "weather";
    public static final String DESCRIPTION_KEY = "description";
    public static final String RESPONSE_STRING = "<b>Temperature:</b> %s °C<br><b>Humidity:</b> %s%%<br><b>Precipitation:</b> %s";
    public static final String TEMPERATURE_FORMAT = "#0.00";
    public static final int KELVIN_IN_CELSIUS = 273;

    @Autowired
    public WeatherController(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @GetMapping
    public String getWeather(@RequestParam("town") String townName) {
        try {
            String getResponse = httpClient.get(
                    String.format(WEATHER_URL, townName),
                    new HashMap<>()
            );
            JsonObject weatherJson = JsonParser.parseString(getResponse).getAsJsonObject();
            JsonObject currentWeather = weatherJson.get(MAIN_KEY).getAsJsonObject();
            JsonElement temperature = currentWeather.get(TEMP_KEY);
            JsonElement humidity = currentWeather.get(HUMIDITY_KEY);
            JsonElement precipitation = weatherJson.get(WEATHER_KEY).getAsJsonArray().get(0).getAsJsonObject().get(DESCRIPTION_KEY);

            return String.format(
                    RESPONSE_STRING,
                    new DecimalFormat(TEMPERATURE_FORMAT).format(Double.parseDouble(temperature.getAsString()) - KELVIN_IN_CELSIUS),
                    humidity.getAsString(),
                    precipitation.getAsString()
            );
        } catch (IOException exception) {
            throw new WeatherApiConnectionException("Can not establish connection with the service.", exception);
        }
    }
}
