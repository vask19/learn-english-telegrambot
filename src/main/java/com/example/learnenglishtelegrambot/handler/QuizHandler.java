package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.Quiz;
import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.repository.UserRepository;
import com.example.learnenglishtelegrambot.service.QuizService;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

import static com.example.learnenglishtelegrambot.util.TelegramUtil.createInlineKeyboardButton;


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


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(CustomUser user, String message) {
        if (message.startsWith(QUIZ_CORRECT)) {
            // действие на коллбек с правильным ответом
            return correctAnswer(user, message);
        } else if (message.startsWith(QUIZ_INCORRECT)) {
            // действие на коллбек с неправильным ответом
            return incorrectAnswer(user);
        } else {
            return startNewQuiz(user);
        }
    }

    private List<PartialBotApiMethod<? extends Serializable>> correctAnswer(CustomUser user, String message) {
        log.info("correct");
       // final int currentScore = user.getScore() + 1;
       // user.setScore(currentScore);
        userRepository.save(user);

        return nextQuestion(user);
    }

    private List<PartialBotApiMethod<? extends Serializable>> incorrectAnswer(CustomUser user) {
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

    private List<PartialBotApiMethod<? extends Serializable>> startNewQuiz(CustomUser user) {
        user.setBotState(State.PLAYING_QUIZ);
        userRepository.save(user);

        return nextQuestion(user);
    }

    private List<PartialBotApiMethod<? extends Serializable>> nextQuestion(CustomUser user) {
        Quiz quiz = quizService.getQuiz();
//
//        // Собираем список возможных вариантов ответа
//        List<String> options = new ArrayList<>(List.of(quiz.getCorrectAnswer(), question.getOptionOne(), question.getOptionTwo(), question.getOptionThree()));
//        // Перемешиваем
//        Collections.shuffle(options);
//
//        // Начинаем формировать сообщение с вопроса
//        StringBuilder sb = new StringBuilder();
//        sb.append('*')
//                .append(question.getQuestion())
//                .append("*\n\n");
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//
//        // Создаем два ряда кнопок
//        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = new ArrayList<>();
//        List<InlineKeyboardButton> inlineKeyboardButtonsRowTwo = new ArrayList<>();
//
//        // Формируем сообщение и записываем CallBackData на кнопки
//        for (int i = 0; i < options.size(); i++) {
//            InlineKeyboardButton button = new InlineKeyboardButton();
//
//            final String callbackData = options.get(i).equalsIgnoreCase(question.getCorrectAnswer()) ? QUIZ_CORRECT : QUIZ_INCORRECT;
//
//            button.setText(OPTIONS.get(i))
//                    .setCallbackData(String.format("%s %d", callbackData, question.getId()));
//
//            if (i < 2) {
//                inlineKeyboardButtonsRowOne.add(button);
//            } else {
//                inlineKeyboardButtonsRowTwo.add(button);
//            }
//            sb.append(OPTIONS.get(i) + ". " + options.get(i));
//            sb.append("\n");
//        }
//
//        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne, inlineKeyboardButtonsRowTwo));
//        return List.of(createMessageTemplate(user)
//                .setText(sb.toString())
//                .setReplyMarkup(inlineKeyboardMarkup));

        return null;
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
