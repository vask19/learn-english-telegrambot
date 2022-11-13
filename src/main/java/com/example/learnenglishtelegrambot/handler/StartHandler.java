package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.BotResponse;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.service.UserService;
import com.example.learnenglishtelegrambot.smiles.Icon;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;


@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {
    @Value("${telegram-bot.name}")
    private String botUsername;

    private final UserService userService;
    private static final String START_COMMAND = "/start";



    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {
        SendMessage welcomeMessage = SendMessage.builder()
                .text(String.format(
                        "Hello! I'm *%s*%nI am here to help you learn some english words " + Icon.BLUSH.get(), botUsername
                ))
                .chatId(String.valueOf(botResponse.getTo()))
                .build();
        welcomeMessage.enableMarkdown(true);

        CustomerUser user = botResponse.getUser();
        user.setBotState(State.NONE);
        userService.save(user);


        return List.of(welcomeMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.START);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(START_COMMAND);
    }
}
