package com.example.learnenglishtelegrambot.handler;


import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

@Component
public interface Handler {

// основной метод, который будет обрабатывать действия пользователя
    public List<PartialBotApiMethod<? extends Serializable>> handle(CustomUser customUser, String message);
    // метод, который позволяет узнать, можем ли мы обработать текущий State у пользователя
    State operatedBotState();
    // метод, который позволяет узнать, какие команды CallBackQuery мы можем обработать в этом классе
    List<String> operatedCallBackQuery();
}

