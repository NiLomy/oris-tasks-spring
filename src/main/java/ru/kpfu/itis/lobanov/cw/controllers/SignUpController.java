package ru.kpfu.itis.lobanov.cw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.lobanov.services.UserService;

@Controller
@RequestMapping("/sign_up")
@RequiredArgsConstructor
public class SignUpController {
    private final UserService userService;

    @GetMapping
    public String getSignUpPage() {
        return "sign_up";
    }
}
