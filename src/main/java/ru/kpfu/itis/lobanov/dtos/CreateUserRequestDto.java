package ru.kpfu.itis.lobanov.dtos;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class CreateUserRequestDto {
    @NotBlank(message = "Name shouldn't be blank.")
    String name;
    @NotBlank(message = "Email shouldn't be blank.")
    @Email(message = "This is invalid email.")
    String email;
    @Size(min = 8, max = 64, message = "Password should contains from 8 to 64 symbols.")
    String password;
}
