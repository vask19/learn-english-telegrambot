package com.example.learnenglishtelegrambot.util;

import com.example.learnenglishtelegrambot.domain.BotResponse;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramUtil {
    public static SendMessage createMessageTemplate(CustomerUser user) {
        return createMessageTemplate(String.valueOf(user.getId()));
    }


    public static SendMessage createMessageTemplate(BotResponse botResponse) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(botResponse.getTo()))
                .text(botResponse.getMessage())
                .build();
        sendMessage.enableMarkdown(true);
        return sendMessage;




    }




    public static SendMessage createMessageTemplate(String chatId,String  text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        sendMessage.enableMarkdown(true);
        return sendMessage;

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
