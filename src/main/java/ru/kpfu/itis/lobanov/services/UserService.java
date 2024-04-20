package ru.kpfu.itis.lobanov.services;

import ru.kpfu.itis.lobanov.dtos.CreateUserRequestDto;
import ru.kpfu.itis.lobanov.dtos.UserDto;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    List<UserDto> findAllByName(String name);

    UserDto createUser(CreateUserRequestDto dto, String url);

    boolean verify(String code);

    void sendVerificationCode(String mail, String name, String code, String baseUrl);

    void updateUser(UserDto userDto);
}
