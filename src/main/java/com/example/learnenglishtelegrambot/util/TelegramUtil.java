package com.example.learnenglishtelegrambot.util;

import com.example.learnenglishtelegrambot.model.CustomUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramUtil {
    public static SendMessage createMessageTemplate(CustomUser user) {
        return createMessageTemplate(String.valueOf(user.getId()));
    }

    // Создаем шаблон SendMessage с включенным Markdown
    public static SendMessage createMessageTemplate(String chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .build();
        sendMessage.enableMarkdown(true);
     return sendMessage;

    }

    // Создаем кнопку
    public static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(command)
                .build();

    }
}
