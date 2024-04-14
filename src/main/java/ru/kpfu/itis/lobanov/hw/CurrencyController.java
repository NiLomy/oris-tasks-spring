package ru.kpfu.itis.lobanov.hw;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.lobanov.hw.exceptions.CurrencyApiConnectionException;
import ru.kpfu.itis.lobanov.hw.httpclient.HttpClient;
import ru.kpfu.itis.lobanov.hw.httpclient.HttpClientImpl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    private final HttpClient httpClient = new HttpClientImpl();
    public static final String CURRENCY_URL = "https://api.currencyapi.com/v3/latest?apikey=cur_live_9tIrbp9F7yqdtjV1dSJCRZl35iJJ7KFUhqYUpoqG";
    public static final String META_KEY = "meta";
    public static final String LAST_UPDATED_KEY = "last_updated_at";
    public static final String DATA_KEY = "data";
    public static final String VALUE_KEY = "value";
    public static final String RESPONSE_STRING = "<b>Statistics on %s</b><br>1 USD = %s %s<br>1 %s = %s USD";
    public static final String CURRENCY_FORMAT = "#0.00";

    @GetMapping
    public String getCurrency(@RequestParam("code") String currencyCode) {
        try {
            String getResponse = httpClient.get(
                    CURRENCY_URL,
                    new HashMap<>()
            );
            JsonObject currencyJson = JsonParser.parseString(getResponse).getAsJsonObject();
            JsonElement currentDate = currencyJson.get(META_KEY).getAsJsonObject().get(LAST_UPDATED_KEY);
            JsonElement currencyValue = currencyJson.get(DATA_KEY).getAsJsonObject().get(currencyCode.toUpperCase()).getAsJsonObject().get(VALUE_KEY);

            return String.format(
                    RESPONSE_STRING,
                    currentDate.getAsString().replace('T', ' ').replace('Z', ' '),
                    new DecimalFormat(CURRENCY_FORMAT).format(currencyValue.getAsDouble()),
                    currencyCode.toUpperCase(),
                    currencyCode.toUpperCase(),
                    new DecimalFormat(CURRENCY_FORMAT).format(1 / currencyValue.getAsDouble())
            );
        } catch (IOException exception) {
            throw new CurrencyApiConnectionException("Can not establish connection with the service.", exception);
        }
    }
}
