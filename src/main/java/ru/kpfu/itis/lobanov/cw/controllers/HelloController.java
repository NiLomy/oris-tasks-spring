package ru.kpfu.itis.lobanov.cw.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.lobanov.aspect.annotations.Loggable;

@RestController
@RequestMapping
public class HelloController {
    @GetMapping("/hello")
    @Loggable
    public String hello() {
        return "Hello!";
    }
}
