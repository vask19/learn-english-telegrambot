package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.service.UserService;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {
    @Value("${bot.name}")
    private String botUsername;
    private final UserService userService;



    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(CustomUser user, String message) {
        // Приветствуем пользователя
        SendMessage welcomeMessage = SendMessage.builder()
                .text(String.format(
                        "Hola! I'm *%s*%nI am here to help you learn Java", botUsername
                ))
                .chatId(String.valueOf(user.getId()))

                .build();
        welcomeMessage.enableMarkdown(true);
        // Просим назваться
        SendMessage registrationMessage = SendMessage.builder()
                .text("In order to start our journey tell me your name")
                .chatId(String.valueOf(user.getId()))
                .build();
        welcomeMessage.enableMarkdown(true);



        // Меняем пользователю статус на - "ожидание ввода имени"
        user.setBotState(State.ENTER_NAME);
        userService.save(user);

        return List.of(welcomeMessage, registrationMessage);
    }

    @Override
    public State operatedBotState() {
        return State.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
