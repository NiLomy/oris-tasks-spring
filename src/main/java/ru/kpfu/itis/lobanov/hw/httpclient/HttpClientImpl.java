package ru.kpfu.itis.lobanov.hw.httpclient;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClientImpl implements HttpClient {
    private String clientToken;

    public HttpClientImpl() {
        this.clientToken = "";
    }

    @Override
    public String get(String url, Map<String, String> params) throws IOException {
        return readOnlyRequestResult(url, params, "GET");
    }

    @Override
    public String post(String url, Map<String, String> params) throws IOException {
        return dataChangeRequestResult(url, params, "POST");
    }

    @Override
    public String put(String url, Map<String, String> params) throws IOException {
        return dataChangeRequestResult(url, params, "PUT");
    }

    @Override
    public String delete(String url, Map<String, String> params) throws IOException {
        return dataChangeRequestResult(url, params, "DELETE");
    }

    private String readOnlyRequestResult(String url, Map<String, String> params, String methodName) throws IOException {
        String paramsLink = getParamsLink(url, params);

        HttpURLConnection connection = (HttpURLConnection) new URL(paramsLink).openConnection();

        connection.setRequestMethod(methodName);
        connection.setRequestProperty("Content-Type", "application/json");

        return getContent(connection);
    }

    private String dataChangeRequestResult(String url, Map<String, String> params, String methodName) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod(methodName);

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + clientToken);

        connection.setDoOutput(true);

        setMethodParams(params, connection);

        return getContent(connection);
    }

    private String getContent(HttpURLConnection connection) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String input;
            while ((input = reader.readLine()) != null) {
                content.append(input);
            }
        }
        connection.disconnect();
        return content.toString();
    }

    private void setMethodParams(Map<String, String> params, HttpURLConnection connection) throws IOException {
        String paramsJson = getParamsJson(params);

        if (paramsJson == null) return;
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = paramsJson.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }
    }

    private String getParamsLink(String url, Map<String, String> params) {
        StringBuilder paramsLink = new StringBuilder();

        paramsLink.append(url).append("?");
        for (Map.Entry<String, String> paramPare :
                params.entrySet()) {
            paramsLink.append(paramPare.getKey()).append("=").append(paramPare.getValue());
            paramsLink.append("&");
        }
        paramsLink.deleteCharAt(paramsLink.length() - 1);
        return paramsLink.toString();
    }

    private String getParamsJson(Map<String, String> params) {
        if (params.isEmpty()) return null;
        StringBuilder paramsJson = new StringBuilder();

        paramsJson.append('{');
        for (Map.Entry<String, String> paramPare :
                params.entrySet()) {
            paramsJson.append('\"').append(paramPare.getKey()).append("\":\"").append(paramPare.getValue()).append('\"');
            paramsJson.append(',');
        }
        paramsJson.deleteCharAt(paramsJson.length() - 1);
        paramsJson.append('}');
        return paramsJson.toString();
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }
}
