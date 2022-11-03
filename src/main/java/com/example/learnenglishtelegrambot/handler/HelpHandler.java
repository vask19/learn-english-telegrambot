package com.example.learnenglishtelegrambot.handler;

import com.whiskels.telegram.bot.State;
import com.whiskels.telegram.model.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.whiskels.telegram.bot.handler.RegistrationHandler.NAME_CHANGE;
import static com.whiskels.telegram.util.TelegramUtil.createInlineKeyboardButton;
import static com.whiskels.telegram.util.TelegramUtil.createMessageTemplate;

@Component
public class HelpHandler implements Handler {

    @Override
    public List<partialbotapimethod<? extends="" serializable="">> handle(User user, String message) {
        // Создаем кнопку для смены имени
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<inlinekeyboardbutton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Change name", NAME_CHANGE));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user).setText(String.format("" +
                "You've asked for help %s? Here it comes!", user.getName()))
        .setReplyMarkup(inlineKeyboardMarkup));

    }

    @Override
    public State operatedBotState() {
        return State.NONE;
    }

    @Override
    public List<string> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
