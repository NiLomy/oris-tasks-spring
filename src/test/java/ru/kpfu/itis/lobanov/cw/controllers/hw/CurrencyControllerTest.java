package ru.kpfu.itis.lobanov.cw.controllers.hw;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.lobanov.services.UserService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kpfu.itis.lobanov.cw.controllers.utils.Constants.USER_NAME;
import static ru.kpfu.itis.lobanov.cw.controllers.utils.Constants.USER_ROLE_ADMIN;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class CurrencyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = USER_NAME, roles = USER_ROLE_ADMIN)
    public void testGetCurrency() throws Exception {
        mockMvc.perform(get("/currency").param("code", "USD"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string(containsString("Statistics on")))
                .andExpect(content().string(containsString("USD")));
    }
}
