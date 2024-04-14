package ru.kpfu.itis.lobanov.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.lobanov.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;

    public static UserDto fromEntity(User save) {
        return new UserDto(save.getName());
    }
}
