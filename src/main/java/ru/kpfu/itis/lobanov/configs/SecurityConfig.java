package ru.kpfu.itis.lobanov.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.kpfu.itis.lobanov.security.AuthProvider;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ComponentScan("ru.kpfu.itis.lobanov.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/index", "/sign_up", "/sign_in").anonymous()
                .antMatchers("/profile").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN");

        http.csrf().disable()
                .formLogin()
                .loginPage("/sign_in")
                .loginProcessingUrl("/login/process")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/profile", true)
                .failureUrl("/sign_in?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/sign_in")
                .and()
                .exceptionHandling();
//                .accessDeniedHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
}
