package ru.kpfu.itis.lobanov.services;

import ru.kpfu.itis.lobanov.dtos.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAllByName(String name);

    UserDto createUser(String name);
}
