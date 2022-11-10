package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.BotResponse;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.service.UserService;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

import static com.example.learnenglishtelegrambot.util.TelegramUtil.createMessageTemplate;


@Component
@RequiredArgsConstructor
@Slf4j
public class HelpHandler implements Handler {

    private static final String HELP_COMMAND = "/help";
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {
        // Создаем кнопку для смены имени


        String text = "" +
                "help h";
        botResponse.setMessage(text);
        CustomerUser customerUser = botResponse.getUser();
        customerUser.setBotState(State.NONE);
        userService.save(customerUser);
        SendMessage sendMessage = createMessageTemplate(botResponse);



        return List.of(sendMessage);
    }


    @Override
    public List<State> operatedBotState() {
        return List.of(State.NONE);
    }


    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(HELP_COMMAND);
    }
}
