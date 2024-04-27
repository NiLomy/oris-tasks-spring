package ru.kpfu.itis.lobanov.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .formLogin()
                .loginPage("/sign_in")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/profile", true)
                .failureUrl("/sign_in?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/sign_in")
                .and()
                .exceptionHandling();

        return httpSecurity.authorizeRequests()
                .antMatchers("/index", "/sign_up", "/sign_in").anonymous()
                .antMatchers("/profile").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
