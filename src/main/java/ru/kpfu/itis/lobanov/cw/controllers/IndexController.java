package ru.kpfu.itis.lobanov.cw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/profile")
    public String getProfilePage() {
        return "profile";
    }
}
