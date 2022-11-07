package com.example.learnenglishtelegrambot.handler.quiz;

import com.example.learnenglishtelegrambot.domain.Quiz;
import com.example.learnenglishtelegrambot.handler.Handler;
import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.model.Word;
import com.example.learnenglishtelegrambot.repository.UserRepository;
import com.example.learnenglishtelegrambot.service.QuizService;
import com.example.learnenglishtelegrambot.service.UserService;
import com.example.learnenglishtelegrambot.service.WordService;
import com.example.learnenglishtelegrambot.telegram.enams.QuizMode;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.*;

import static com.example.learnenglishtelegrambot.handler.quiz.NextQuizHandler.NEXT_WORD;
import static com.example.learnenglishtelegrambot.util.TelegramUtil.createInlineKeyboardButton;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartingQuizHandler implements Handler {
    public static final String STARTING_QUIZ = "/starting_quiz";


    private final UserRepository userRepository;
    private final QuizService quizService;
    private final UserService userService;

    private static final int QUIZ_SIZE = 3;
    private static final QuizMode QUIZ_MODE = QuizMode.NORMAL;
    private final WordService wordService;
    private Quiz quiz;
    private String lastAnswer;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(CustomUser customUser, String message) {
        System.out.println("HELLO");
        quiz = initQuiz(customUser);
        System.out.println(quiz);


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Next Word", NEXT_WORD));


        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        SendMessage sendMessage = SendMessage.builder()
                .replyMarkup(inlineKeyboardMarkup)
                .chatId(String.valueOf(customUser.getId()))
                .text("Starting...")
                .build();

        return List.of(sendMessage);


    }

    @Override
    public State operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(STARTING_QUIZ);
    }


    @Bean
    @SessionScope
    public Quiz getQuiz(){
        return new Quiz();
    }



    public Quiz initQuiz(CustomUser user){
        Quiz quiz = new Quiz();

        Random random = new Random();
        Set<Word> quizWords = new HashSet<>();
        List<Word> words = new ArrayList<>();


        switch (QUIZ_MODE){
            case NEW -> words = wordService.getNewWords(user);
            case NORMAL -> words = wordService.getAllWordsByUser(user);
        }
        while (quizWords.size() < QUIZ_SIZE & QUIZ_SIZE <= words.size()){
            quizWords.add(words.get(random.nextInt(words.size())));

        }
        quiz.setQueue(new LinkedList<>(quizWords));

        return quiz;

    }
}
