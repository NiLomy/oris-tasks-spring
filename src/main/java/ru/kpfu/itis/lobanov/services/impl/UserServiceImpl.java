package ru.kpfu.itis.lobanov.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.lobanov.dtos.UserDto;
import ru.kpfu.itis.lobanov.model.User;
import ru.kpfu.itis.lobanov.repositories.UserRepository;
import ru.kpfu.itis.lobanov.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAllByName(String name) {
        return userRepository.findAllByName(name)
                .stream().map(user -> new UserDto(user.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(String name) {
        return new UserDto(userRepository.save(User.builder().id(1L).name(name).build()).getName());
    }
}
