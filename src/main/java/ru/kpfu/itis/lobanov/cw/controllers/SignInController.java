package ru.kpfu.itis.lobanov.cw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.lobanov.services.UserService;

@Controller
@RequestMapping("/sign_in")
public class SignInController {

    @GetMapping
    public String getSignInPage() {
        return "sign_in";
    }
}
