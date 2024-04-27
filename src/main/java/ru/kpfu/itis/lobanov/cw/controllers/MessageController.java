package ru.kpfu.itis.lobanov.cw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kpfu.itis.lobanov.dtos.MessageDto;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations messageTemplate;

    @GetMapping("/chat/{id}")
    public String getChat(@PathVariable("id") String pageId, Model model) {
        model.addAttribute("pageId", pageId);
        return "chat";
    }

    @MessageMapping("/message")
    public void message(MessageDto message) {
        messageTemplate.convertAndSend("/topic/message/" + message.getPageId() , new MessageDto(message.getText(), message.getSender(), message.getPageId()));
    }
}