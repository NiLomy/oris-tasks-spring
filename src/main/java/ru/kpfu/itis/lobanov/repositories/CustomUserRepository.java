package ru.kpfu.itis.lobanov.repositories;

import ru.kpfu.itis.lobanov.model.User;

import java.util.List;

public interface CustomUserRepository {
    List<User> findAllByNameMatch(String name, double factor);
}
