package ru.kpfu.itis.lobanov.cw.controllers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.kpfu.itis.lobanov.services.UserService;

import java.util.Properties;

@TestConfiguration
@Configuration
public class TestConfig {
    @MockBean
    private UserService userService;
}
