package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.Quiz;
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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.*;

import static com.example.learnenglishtelegrambot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.learnenglishtelegrambot.util.TelegramUtil.createMessageTemplate;


@Slf4j
@Component
@RequiredArgsConstructor
public class QuizHandler implements Handler {
    //Храним поддерживаемые CallBackQuery в виде констант
    public static final String QUIZ_CORRECT = "/quiz_correct";
    public static final String QUIZ_INCORRECT = "/quiz_incorrect";
    public static final String QUIZ_START = "/quiz_start";
    //Храним варианты ответа
    private static final List<String> OPTIONS = List.of("A", "B", "C", "D");

    private final UserRepository userRepository;
    private final QuizService quizService;
    private final UserService userService;

    private static final int QUIZ_SIZE = 3;
    private static final QuizMode QUIZ_MODE = QuizMode.NORMAL;
    private final WordService wordService;
    private Quiz quiz;
    private String lastAnswer;


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(CustomUser user, String message) {
        log.info("quiz handler started");
        if (lastAnswer != null) {
            if (lastAnswer.equals(QUIZ_CORRECT)) {
                // действие на коллбек с правильным ответом
                return correctAnswer(user, message);
            } else if (lastAnswer.equals(QUIZ_INCORRECT)) {
                // действие на коллбек с неправильным ответом
                return incorrectAnswer(user, message);
            }
        }
        return startNewQuiz(user, message);


    }





    private List<PartialBotApiMethod<? extends Serializable>> correctAnswer(CustomUser user, String message) {
        log.info("correct");
       // final int currentScore = user.getScore() + 1;
       // user.setScore(currentScore);
        userRepository.save(user);

        return nextQuestion(user,message);
    }

    private List<PartialBotApiMethod<? extends Serializable>> incorrectAnswer(CustomUser user,String message) {
//        final int currentScore = user.getScore();
//        // Обновляем лучший итог
//        if (user.getHighScore() < currentScore) {
//            user.setHighScore(currentScore);
//        }
//        // Меняем статус пользователя
//        user.setScore(0);


        //user.setBotState(State.NONE);
        userRepository.save(user);

        // Создаем кнопку для повторного начала игры
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Try again?", QUIZ_START));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));


        SendMessage sendMessage = SendMessage.builder()
                .replyMarkup(inlineKeyboardMarkup)
                .text(String.format("Incorrect!%nYou scored *%d* points!", 0))
                .build();

        return List.of(sendMessage);

    }

   private List<PartialBotApiMethod<? extends Serializable>> startNewQuiz(CustomUser user,String message) {
        user.setBotState(State.PLAYING_QUIZ);
        user = userService.save(user);

        initQuiz(user);
        return nextQuestion(user,message);
    }

    private List<PartialBotApiMethod<? extends Serializable>> nextQuestion(CustomUser user,String message) {


        // Начинаем формировать сообщение с вопроса
        Word nextWord = quiz.next();
        String callbackData = "";

        System.out.println(message);

        System.out.println("------" + nextWord);
        if (message != null){
            callbackData = message.equals(nextWord.getValue()) ? QUIZ_CORRECT : QUIZ_INCORRECT;

        }
        lastAnswer = callbackData;
        System.out.println(lastAnswer);
        String text = "rr";
        SendMessage sendMessage = createMessageTemplate(user, text);


        return List.of(sendMessage);

    }


    //TODO : abstract fabric
    public void initQuiz(CustomUser user){
        this.quiz = new Quiz();

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

    }







    @Override
    public State operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(QUIZ_START, QUIZ_CORRECT, QUIZ_INCORRECT);
    }
}
