package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.example.learnenglishtelegrambot.handler.RegistrationHandler.NAME_CHANGE;
import static com.example.learnenglishtelegrambot.util.TelegramUtil.createInlineKeyboardButton;


@Component
public class HelpHandler implements Handler {

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(CustomUser user, String message) {
        // Создаем кнопку для смены имени
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Change name", NAME_CHANGE));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));



        SendMessage sendMessage = SendMessage.builder()
                .replyMarkup(inlineKeyboardMarkup)
                .text(String.format("" +
                                "You've asked for help %s? Here it comes!", user.getName()))
                .build();


        return List.of(sendMessage);
    }


    @Override
    public State operatedBotState() {
        return State.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
