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
import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<Long,Score> scoreHashMap;
    private HashMap<Long,Word> currentWords;
//    private Word currentWord;
//    private Score currentScore;

    {
        scoreHashMap = new HashMap<>();
        currentWords = new HashMap<>();
    }


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {

        if (message.equals(START_QUIZ)){
            return startQuiz(botResponse,message);
        }
        else  if(!message.equals("end")) return nextWord(botResponse,message);
        else return endQuiz(botResponse,message);

    }



    private List<PartialBotApiMethod<? extends Serializable>> startQuiz(BotResponse botResponse, String message) {
        CustomerUser customerUser = botResponse.getUser();
        Score currentScore = new Score();
        currentScore.initWordLists();
        scoreHashMap.put(customerUser.getId(),currentScore);

        quizService.deleteQuiz(customerUser);
        Quiz quiz = quizService.createQuiz(customerUser);

        customerUser.setBotState(State.QUIZ_HANDLER);
        userService.save(customerUser);

        return nextWord(botResponse,START_QUIZ);

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



        return wordProcessing(newWord,botResponse,message);
    }



    private List<PartialBotApiMethod<? extends Serializable>> wordProcessing(Word newWord, BotResponse botResponse, String  message){
        SendMessage firstMessage = null;
        SendMessage secondMessage = null;
        botResponse.setMessage("");
        if (!message.equals(START_QUIZ)){
            boolean answerResult = checkAnswer(message,botResponse.getUser().getId());
            secondMessage = createSendMessageForCurrentWord(newWord,botResponse,message,answerResult);

        }
        currentWords.put(botResponse.getUser().getId(),newWord);

        firstMessage = createSendMessageForNewWord(newWord,botResponse);

        List<PartialBotApiMethod<? extends Serializable>> answerList = new ArrayList<>();
        if (firstMessage != null){
            answerList.add(firstMessage);

        }
        if (secondMessage != null){
            answerList.add(secondMessage);
        }

        return answerList;



    }


    private SendMessage createSendMessageForNewWord(Word newWord,BotResponse botResponse){
        String newWordText = String.format("New word:\n%s",decor(newWord.getTranslation()));
        botResponse.setMessage(newWordText);
        SendMessage newWordMessage = createMessageTemplate(botResponse);
        newWordMessage.setParseMode("HTML");
        return newWordMessage;

    }

    private SendMessage createSendMessageForCurrentWord(Word newWord, BotResponse botResponse, String  message,boolean answerResult){
        Word currentWord = currentWords.get(botResponse.getUser().getId());
        String resultText = createResultMessage(message,currentWord.getValue(),answerResult);
        botResponse.setMessage(resultText);
        SendMessage sendMessage = createMessageTemplate(botResponse);
        sendMessage.setParseMode("HTML");


        return  sendMessage;


    }





    private String createResultMessage(String first, String second,boolean result){
        StringBuilder sb = new StringBuilder("Your answer ");
        sb.append(result ? "<b>is correct</b>" : "<b>isn't correct</b>");
        sb.append("\n");
        sb.append("your answer: ").append(decor(first)).append("\n");
        sb.append("correct answer: ").append(decor(second)).append("\n");
        return sb.toString();
    }

    private boolean checkAnswer(String  guessWord,Long userId){
        Word currentWord = currentWords.get(userId);
        Score currentScore = scoreHashMap.get(userId);
        if (guessWord.equals(currentWord.getValue())){
            currentScore.getCorrectAnswers().add(currentWord);
            return true;
        }
        currentScore.getIncorrectAnswers().add(currentWord);
        return false;

    }








    private List<PartialBotApiMethod<? extends Serializable>> endQuiz(BotResponse botResponse, String message) {
        Long userId = botResponse.getUser().getId();
        boolean lastAnswerResult = checkAnswer(message,userId);
        Score currentScore = scoreHashMap.get(userId);

        Score score = scoreService.saveScore(currentScore,botResponse.getUser());
        String text = score.toString();
        botResponse.setMessage(text);
        SendMessage endMessage = createMessageTemplate(botResponse);
        endMessage.setParseMode("HTML");
        CustomerUser user = botResponse.getUser();
        user.setBotState(State.NONE);
        userService.save(user);
        return List.of(endMessage);
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
