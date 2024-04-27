package ru.kpfu.itis.lobanov.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto {
    private String text;
    private String sender;
    private String pageId;
}
