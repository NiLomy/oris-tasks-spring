package ru.kpfu.itis.lobanov.cw.controllers.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kpfu.itis.lobanov.configs.MailConfig;
import ru.kpfu.itis.lobanov.cw.controllers.TestConfig;
import ru.kpfu.itis.lobanov.dtos.CreateUserRequestDto;
import ru.kpfu.itis.lobanov.dtos.UserDto;
import ru.kpfu.itis.lobanov.model.Role;
import ru.kpfu.itis.lobanov.model.User;
import ru.kpfu.itis.lobanov.repositories.UserRepository;
import ru.kpfu.itis.lobanov.services.UserService;
import ru.kpfu.itis.lobanov.services.impl.UserServiceImpl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.kpfu.itis.lobanov.cw.controllers.utils.Constants.*;

@Import({TestConfig.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
    private static UserService userService;
    private static UserDto userDto;
    private static UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUpService() {
        userRepository = Mockito.mock(UserRepository.class);

        userDto = new UserDto(USER_NAME);
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ROLE_ID, USER_ROLE_ADMIN, null));
        User user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD, true, null, roles);

        when(userRepository.findAllByName(USER_NAME)).thenReturn(Collections.singletonList(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByVerificationCode(SUCCESS_VERIFICATION_CODE)).thenReturn(Optional.of(user));
        when(userRepository.findByVerificationCode(FAILED_VERIFICATION_CODE)).thenReturn(Optional.empty());
        userService = new UserServiceImpl(userRepository, mailConfig, javaMailSender, passwordEncoder);
    }

    @Test
    public void testFindAllByName() {
        assert userService.findAllByName(USER_NAME).equals(Collections.singletonList(userDto));
        verify(userRepository, times(1)).findAllByName(USER_NAME);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testUserCreation() {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto(USER_NAME, USER_EMAIL, USER_PASSWORD);
        UserDto user = userService.createUser(createUserRequestDto, BASE_URL);
        assertEquals(user, userDto);
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testSuccessfulVerification() {
        assert userService.verify(SUCCESS_VERIFICATION_CODE);
        verify(userRepository, times(1)).findByVerificationCode(SUCCESS_VERIFICATION_CODE);
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFailedVerification() {
        assertFalse(userService.verify(FAILED_VERIFICATION_CODE));
        verify(userRepository, times(1)).findByVerificationCode(FAILED_VERIFICATION_CODE);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void sendMessage() {
        userService.sendVerificationCode(USER_EMAIL, USER_NAME, SUCCESS_VERIFICATION_CODE, BASE_URL);
    }
}
