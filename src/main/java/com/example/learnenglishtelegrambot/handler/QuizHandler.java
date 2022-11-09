package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.BotResponse;
import com.example.learnenglishtelegrambot.model.Quiz;
import com.example.learnenglishtelegrambot.service.WordService;
import com.example.learnenglishtelegrambot.telegram.enams.QuizMode;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.example.learnenglishtelegrambot.util.TelegramUtil.createMessageTemplate;


@Slf4j
@Component
@RequiredArgsConstructor
public class QuizHandler implements Handler {



    private static final int QUIZ_SIZE = 3;
    private static final QuizMode QUIZ_MODE = QuizMode.NORMAL;
    private final WordService wordService;
    private Quiz quiz;
    private String lastAnswer;


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {

        String text = "" +
                "You've asked for help ? Here it comes!";
        botResponse.setMessage(text);
        SendMessage sendMessage = createMessageTemplate(botResponse);

        String text2 = "" +
                "You've asked for help ? Here it comes!";
        botResponse.setMessage(text);
        SendMessage sendMessage2 = createMessageTemplate(botResponse);


        return List.of(sendMessage,sendMessage2);
     //   return startNewQuiz(botResponse, message);


    }

















    @Override
    public State operatedBotState() {
        return State.QUIZ_HANDLER;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
