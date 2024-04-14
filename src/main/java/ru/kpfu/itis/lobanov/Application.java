package ru.kpfu.itis.lobanov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application {

    public Application(FreeMarkerConfigurer configurer) {
        List<String> a = Arrays.asList("/META-INF/security.tld");
        configurer.getTaglibFactory().setClasspathTlds(a);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
