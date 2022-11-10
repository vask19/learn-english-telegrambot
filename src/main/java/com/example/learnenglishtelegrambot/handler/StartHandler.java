package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.BotResponse;
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

    private static final String START_COMMAND = "/start";



    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {
        // Приветствуем пользователя
        SendMessage welcomeMessage = SendMessage.builder()
                .text(String.format(
                        "Hola! I'm *%s*%nI am here to help you learn Java", botUsername
                ))
                .chatId(String.valueOf(botResponse.getTo()))

                .build();
        welcomeMessage.enableMarkdown(true);
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
