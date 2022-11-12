package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.BotResponse;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.service.UserService;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.learnenglishtelegrambot.util.TelegramUtil.createMessageTemplate;


@Component
@RequiredArgsConstructor
@Slf4j
public class HelpHandler implements Handler {

    private static final List<String> helpAnswersText = List.of("Commands:\n","[/start_quiz] - run new quiz\n","[/new] - add new word");
    private static final String HELP_COMMAND = "/help";
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {
        // Создаем кнопку для смены имени




        CustomerUser customerUser = botResponse.getUser();
        customerUser.setBotState(State.NONE);
        userService.save(customerUser);



        return createMessages(botResponse);
    }





    private List<PartialBotApiMethod<? extends Serializable>> createMessages(BotResponse botResponse){
       return helpAnswersText
                .stream()
                .map( text -> {
                            botResponse.setMessage(text);
                            return createMessageTemplate(botResponse);
                        }
                ).collect(Collectors.toList());

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
