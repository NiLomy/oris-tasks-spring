package ru.kpfu.itis.lobanov.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kpfu.itis.lobanov.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
//    List<User> findAllByNameContainsIgnoreCaseAndIdLessThan(String name, Long id);
    List<User> findAllByName(String name);

    @Query(value = "select * from users u.name = ?1", nativeQuery = true)
    List<User> findAllByNameSQL(String name);

    @Query(value = "select u from User u where u.name = :name")
    List<User> findAllByNameJPQL(String name);
}
