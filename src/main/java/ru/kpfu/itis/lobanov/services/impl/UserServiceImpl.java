package ru.kpfu.itis.lobanov.services.impl;

import org.springframework.stereotype.Service;
import ru.kpfu.itis.lobanov.dtos.UserDto;
import ru.kpfu.itis.lobanov.services.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserDto> findAllByName(String name) {
        return null;
    }
}
