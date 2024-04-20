package ru.kpfu.itis.lobanov.cw.controllers.controllers;

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
import ru.kpfu.itis.lobanov.cw.controllers.IndexController;
import ru.kpfu.itis.lobanov.services.UserService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kpfu.itis.lobanov.cw.controllers.utils.Constants.*;

@WebMvcTest(IndexController.class)
@Import(SecurityConfig.class)
@ExtendWith(SpringExtension.class)
public class IndexControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetSignUpPage() throws Exception {
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("example")));
    }

    @Test
    public void testGetProfilePage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("profile"));
    }

    @Test
    public void testUnauthorizedProfile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isUnauthorized());
    }
}
