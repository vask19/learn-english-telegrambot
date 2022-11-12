package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.BotResponse;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.model.Quiz;
import com.example.learnenglishtelegrambot.model.Score;
import com.example.learnenglishtelegrambot.model.Word;
import com.example.learnenglishtelegrambot.service.QuizService;
import com.example.learnenglishtelegrambot.service.ScoreService;
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
import static com.example.learnenglishtelegrambot.utils.AnswerDecorator.decor;


@Slf4j
@Component
@RequiredArgsConstructor
public class QuizHandler implements Handler {

    private static final int QUIZ_SIZE = 3;
    private static final QuizMode QUIZ_MODE = QuizMode.NORMAL;
    private final WordService wordService;
    private final UserService userService;
    private final QuizService quizService;
    private final ScoreService scoreService;
    private final String START_QUIZ = "/start_quiz";
    private Word currentWord;
    private Score currentScore;


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {

        if (message.equals(START_QUIZ)){
            return startQuiz(botResponse,message);
        }
        else  if(!message.equals("end")) return nextWord(botResponse,message);
        else return endQuiz(botResponse,message);

    }

    private List<PartialBotApiMethod<? extends Serializable>> endQuiz(BotResponse botResponse, String message) {
        boolean lastAnswerResult = checkAnswer(message);
        Score score = scoreService.saveScore(currentScore,botResponse.getUser());
        System.out.println(score.getIncorrectAnswers());
        System.out.println(score.getCorrectAnswers());
        String text = "" +
                "END!!!";
        botResponse.setMessage(text);
        SendMessage sendMessage = createMessageTemplate(botResponse);
        return List.of(sendMessage);
    }

    public List<PartialBotApiMethod<? extends Serializable>> nextWord(BotResponse botResponse, String message) {
        CustomerUser user = botResponse.getUser();
        Quiz quiz = quizService.getQuiz(user);
        List<Long> wordIds= quiz.getWordIds();
        if (wordIds.size() == 0){
            return endQuiz(botResponse,message);
        }
        Word newWord = wordService.getWord(wordIds.get(0));
        quiz.getWordIds().remove(0);

        quizService.saveQuiz(quiz);
        user.setQuiz(quiz);
        userService.save(user);



        return createAnswerToUser(newWord,botResponse,message);
    }



    private List<PartialBotApiMethod<? extends Serializable>> createAnswerToUser(Word newWord, BotResponse botResponse, String  message){
        String newWordText = String.format("New word:\n%s",decor(newWord.getTranslation()));
        botResponse.setMessage("");

        //if message isn't a start message - create message which users answer result
        if (!message.equals(START_QUIZ)){
            boolean answerResult = checkAnswer(message);
            String resultText = createResultMessage(message,currentWord.getValue(),answerResult);
            botResponse.setMessage(resultText);
        }

        SendMessage checkWordMessage = createMessageTemplate(botResponse);
        checkWordMessage.setParseMode("HTML");

        currentWord = newWord;

        botResponse.setMessage(newWordText);
        SendMessage newWordMessage = createMessageTemplate(botResponse);
        newWordMessage.setParseMode("HTML");
        return List.of(checkWordMessage,newWordMessage);

    }

    private String createResultMessage(String first, String second,boolean result){
        StringBuilder sb = new StringBuilder("Your answer ");
        sb.append(result ? "<b>is correct</b>" : "<b>isn't correct</b>");
        sb.append("\n");
        sb.append("your answer: ").append(decor(first)).append("\n");
        sb.append("correct answer: ").append(decor(second)).append("\n");
        return sb.toString();
    }

    private boolean checkAnswer(String  guessWord){
        if (guessWord.equals(currentWord.getValue())){
            currentScore.getCorrectAnswers().add(currentWord);
            return true;
        }
        currentScore.getIncorrectAnswers().add(currentWord);
        return false;

    }



    private List<PartialBotApiMethod<? extends Serializable>> startQuiz(BotResponse botResponse, String message) {
        String text = "" +
                "START!!!";

        CustomerUser customerUser = botResponse.getUser();
        currentScore = new Score();
        currentScore.initWordLists();

        quizService.deleteQuiz(customerUser);
        Quiz quiz = quizService.createQuiz(customerUser);

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
