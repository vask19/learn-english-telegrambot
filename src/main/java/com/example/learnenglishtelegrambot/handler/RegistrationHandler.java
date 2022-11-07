package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.service.UserService;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

import static com.example.learnenglishtelegrambot.handler.quiz.StartingQuizHandler.*;
import static com.example.learnenglishtelegrambot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.learnenglishtelegrambot.util.TelegramUtil.createMessageTemplate;


@Component
@RequiredArgsConstructor
public class RegistrationHandler implements Handler {
    //Храним поддерживаемые CallBackQuery в виде констант
    public static final String NAME_ACCEPT = "/enter_name_accept";
    public static final String NAME_CHANGE = "/enter_name";
    public static final String NAME_CHANGE_CANCEL = "/enter_name_cancel";
    private CustomUser customUser;

    private final UserService userService;


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(CustomUser user, String message) {
        // Проверяем тип полученного события
        if (message.equalsIgnoreCase(NAME_ACCEPT) || message.equalsIgnoreCase(NAME_CHANGE_CANCEL)) {
            return accept(user);
        } else if (message.equalsIgnoreCase(NAME_CHANGE)) {
            return changeName(user);
        }
        return checkName(user, message);

    }

    private  List<PartialBotApiMethod<? extends Serializable>> accept(CustomUser user) {
        // Если пользователь принял имя - меняем статус и сохраняем
        user.setBotState(State.NONE);
        userService.save(user);

        // Создаем кнопку для начала игры
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
            createInlineKeyboardButton("Start quiz", STARTING_QUIZ));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(user.getId()))
                .replyMarkup(inlineKeyboardMarkup)
                .text(String.format(
                "Your name is saved as: %s", user.getName()))
                .build();

        return List.of(sendMessage);

    }

    private  List<PartialBotApiMethod<? extends Serializable>> checkName(CustomUser user, String message) {
        // При проверке имени мы превентивно сохраняем пользователю новое имя в базе
        // идея для рефакторинга - добавить временное хранение имени
        user.setName(message);
        userService.save(user);

        // Делаем кнопку для применения изменений
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Accept", NAME_ACCEPT));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        SendMessage sendMessage = SendMessage.builder()
                .replyMarkup(inlineKeyboardMarkup)
                .chatId(String.valueOf(user.getId()))
                .text(String.format("You have entered: %s%nIf this is correct - press the button", user.getName()))
                .build();

        return List.of(sendMessage);

    }

    private  List<PartialBotApiMethod<? extends Serializable>> changeName(CustomUser user) {
        // При запросе изменения имени мы меняем State
        user.setBotState(State.ENTER_NAME);
        userService.save(user);


        // Создаем кнопку для отмены операции
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Cancel", NAME_CHANGE_CANCEL));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));


        String  text =String.format(
                "Your current name is: %s%nEnter new name or press the button to continue", user.getName());
        SendMessage sendMessage = createMessageTemplate(user,text);

        return List.of(sendMessage);
    }

    @Override
    public State operatedBotState() {
        return State.ENTER_NAME;
    }


    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(NAME_ACCEPT, NAME_CHANGE, NAME_CHANGE_CANCEL);
    }
}
