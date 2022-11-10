package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.BotResponse;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.model.Quiz;
import com.example.learnenglishtelegrambot.model.Word;
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

import java.io.Serializable;
import java.util.List;

import static com.example.learnenglishtelegrambot.util.TelegramUtil.createMessageTemplate;


@Slf4j
@Component
@RequiredArgsConstructor
public class QuizHandler implements Handler {





    private static final int QUIZ_SIZE = 3;
    private static final QuizMode QUIZ_MODE = QuizMode.NORMAL;
    private final WordService wordService;
    private final UserService userService;
    private final QuizService quizService;
    private final String START_QUIZ = "/start_quiz";
    private Word currentWord;




    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {

        if (message.equals(START_QUIZ)){
            return startQuiz(botResponse,message);
        }
        else  if(!message.equals("end")) return nextWord(botResponse,message);
        else return endQuiz(botResponse,message);

    }

    private List<PartialBotApiMethod<? extends Serializable>> endQuiz(BotResponse botResponse, String message) {
        String text = "" +
                "END!!!";
        botResponse.setMessage(text);
        SendMessage sendMessage = createMessageTemplate(botResponse);
        return List.of(sendMessage);
    }

    private List<PartialBotApiMethod<? extends Serializable>> nextWord(BotResponse botResponse, String message) {
        CustomerUser user = botResponse.getUser();
        Quiz quiz = quizService.getQuiz(user);
        List<Long> wordIds= quiz.getWordIds();
        Word newWord = wordService.getWord(wordIds.get(0));
        quiz.getWordIds().remove(0);
        quizService.saveQuiz(quiz);

        String newWordText = String.format("New ford:\n%s",newWord.getTranslation());
        botResponse.setMessage("");



        if (!message.equals(START_QUIZ)){

            botResponse.setMessage(message.equals(currentWord.getValue()) + "");

        }

        SendMessage checkWordMessage = createMessageTemplate(botResponse);

        currentWord = newWord;

        botResponse.setMessage(newWordText);
        SendMessage newWordMessage = createMessageTemplate(botResponse);

        return List.of(checkWordMessage,newWordMessage);
    }

    private List<PartialBotApiMethod<? extends Serializable>> startQuiz(BotResponse botResponse, String message) {
        String text = "" +
                "START!!!";
        CustomerUser customerUser = botResponse.getUser();

        Quiz quiz = quizService.getQuiz(customerUser);

        customerUser.setBotState(State.QUIZ_HANDLER);
        userService.save(customerUser);

        return nextWord(botResponse,"/start_quiz");
//
//        botResponse.setMessage(text);
//        SendMessage sendMessage = createMessageTemplate(botResponse);
//        return List.of(sendMessage);
    }


    @Override
    public List<State> operatedBotState() {
        return List.of(State.START,State.QUIZ_HANDLER);
    }


    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(START_QUIZ);
    }
}
