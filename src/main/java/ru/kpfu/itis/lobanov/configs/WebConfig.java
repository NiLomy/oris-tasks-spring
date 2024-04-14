package ru.kpfu.itis.lobanov.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kpfu.itis.lobanov.hw.httpclient.HttpClient;
import ru.kpfu.itis.lobanov.hw.httpclient.HttpClientImpl;

@Configuration
public class WebConfig {
    @Bean
    public HttpClient httpClient() {
        return new HttpClientImpl();
    }
}
