package com.example.learnenglishtelegrambot.telegram;

import com.example.learnenglishtelegrambot.handler.UpdateReceiver;
import com.example.learnenglishtelegrambot.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Component
@Data
public class LearnEnglishBot extends TelegramLongPollingBot {
    private String botUsername;

    private String botToken;

    private final UserService userService;
    private final UpdateReceiver updateReceiver;
    private final TelegramBotsApi telegramBotsApi;

    public LearnEnglishBot(@Value("${telegram-bot.name}") String botUsername, @Value("${telegram-bot.token}") String botToken,
                           UserService userService, UpdateReceiver updateReceiver, TelegramBotsApi telegramBotsApi) throws TelegramApiException {
        this.userService = userService;
        this.updateReceiver = updateReceiver;
        this.telegramBotsApi = telegramBotsApi;
        this.botToken = botToken;
        this.botUsername = botUsername;
        telegramBotsApi.registerBot(this);
    }


    @Override
    public void onUpdateReceived(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);

        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage) {
                    executeWithExceptionCheck((SendMessage) response);
                }
            });
        }
    }



    public void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("oops");
        }
    }
}
