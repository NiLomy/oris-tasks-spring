package ru.kpfu.itis.lobanov.cw.controllers.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.lobanov.configs.SecurityConfig;
import ru.kpfu.itis.lobanov.dtos.CreateUserRequestDto;
import ru.kpfu.itis.lobanov.dtos.UserDto;
import ru.kpfu.itis.lobanov.services.UserService;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static ru.kpfu.itis.lobanov.cw.controllers.utils.Constants.*;

@WebMvcTest
@Import(SecurityConfig.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static UserDto userDto;

    @BeforeAll
    public static void createUserDto() {
        userDto = new UserDto(USER_NAME);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        given(userService.findAllByName(USER_NAME)).willReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/users/all?name=" + USER_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(USER_NAME).roles(USER_ROLE_ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(USER_NAME));
        verify(userService, times(1)).findAllByName(USER_NAME);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetSignUpPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("sign_up"))
                .andExpect(content().string(containsString("Sign up")));
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = USER_ROLE_ADMIN)
    public void testVerificationSuccess() throws Exception {
        given(userService.verify(SUCCESS_VERIFICATION_CODE)).willReturn(true);

        mockMvc.perform(get("/users/verification?code=" + SUCCESS_VERIFICATION_CODE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("verification_success"))
                .andExpect(content().string(containsString("Verification success")));

        verify(userService, times(1)).verify(SUCCESS_VERIFICATION_CODE);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = USER_ROLE_ADMIN)
    public void testVerificationFailed() throws Exception {
        given(userService.verify(FAILED_VERIFICATION_CODE)).willReturn(false);

        mockMvc.perform(get("/users/verification?code=" + FAILED_VERIFICATION_CODE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("verification_failed"))
                .andExpect(content().string(containsString("Verification failed")));

        verify(userService, times(1)).verify(FAILED_VERIFICATION_CODE);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = USER_ROLE_ADMIN)
    public void testCreationOfUser() throws Exception {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto(null, null, null);
        given(userService.createUser(createUserRequestDto, BASE_URL)).willReturn(userDto);

        Gson gson = new Gson();
        mockMvc.perform(post("/users").with(csrf()).content(gson.toJson(createUserRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("sign_up_success"))
                .andExpect(content().string(containsString("SUCCESS")));

        verify(userService, times(1)).createUser(createUserRequestDto, BASE_URL);
        verifyNoMoreInteractions(userService);
    }
}
