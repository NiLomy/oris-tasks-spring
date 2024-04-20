package ru.kpfu.itis.lobanov.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.lobanov.dtos.UserDto;

@Aspect
@Order(1)
@Component
public class UserInfoValidationAspect {
    @Pointcut("@annotation(ru.kpfu.itis.lobanov.aspect.annotations.ValidateUserInfo)")
    public void validateUserInfo() {
    }

    @Before("validateUserInfo()")
    public void validate(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length < 1) throw new RuntimeException("Argument of user info should be passed");

        Object arg = joinPoint.getArgs()[0];

        if (arg == null) throw new RuntimeException("User info should not be null");

        if (arg instanceof UserDto) {
            UserDto userDto = (UserDto) arg;
            String name = userDto.getName();
            if (name == null) throw new RuntimeException("Name of user shouldn't be null");
            if (name.trim().isEmpty()) throw new RuntimeException("Name of user shouldn't be empty");
        } else throw new RuntimeException("This is not user info");
    }
}
