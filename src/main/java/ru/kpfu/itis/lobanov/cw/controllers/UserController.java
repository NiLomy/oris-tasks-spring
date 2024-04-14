package ru.kpfu.itis.lobanov.cw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.lobanov.dtos.CreateUserRequestDto;
import ru.kpfu.itis.lobanov.dtos.UserDto;
import ru.kpfu.itis.lobanov.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ResponseBody
    @GetMapping("/all")
    public List<UserDto> findAllByName(@RequestParam("name") String name) {
        return userService.findAllByName(name);
    }

    @GetMapping
    public String getSignUpPage() {
        return "sign_up";
    }

    @PostMapping
    public String create(@ModelAttribute CreateUserRequestDto user, HttpServletRequest req) {
        String url = req.getRequestURL().toString().replace(req.getServletPath(), "");
        userService.createUser(user, url);
        return "sign_up_success";
    }

    @GetMapping("/verification")
    public String verify(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verification_success";
        }
        return "verification_failed";
    }
}
