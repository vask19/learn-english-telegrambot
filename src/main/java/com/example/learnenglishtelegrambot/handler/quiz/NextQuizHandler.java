package com.example.learnenglishtelegrambot.handler.quiz;

import com.example.learnenglishtelegrambot.domain.Quiz;
import com.example.learnenglishtelegrambot.handler.Handler;
import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class NextQuizHandler implements Handler {
    public static final String NEXT_WORD = "/next_word";
    private final Quiz quiz;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(CustomUser customUser, String message) {
        System.out.println("next");
        System.out.println(quiz.getQueue());
        System.out.println(message);
        return null;
    }

    @Override
    public State operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(NEXT_WORD);
    }
}
