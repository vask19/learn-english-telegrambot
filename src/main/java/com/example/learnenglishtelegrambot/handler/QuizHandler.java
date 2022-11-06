package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.Quiz;
import com.example.learnenglishtelegrambot.exceptions.QuizEmptyException;
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

import java.io.Serializable;
import java.util.*;

import static com.example.learnenglishtelegrambot.util.TelegramUtil.createMessageTemplate;


@Slf4j
@Component
@RequiredArgsConstructor
public class QuizHandler implements Handler {
    //Храним поддерживаемые CallBackQuery в виде констант
    public static final String QUIZ_CORRECT = "/quiz_correct";
    public static final String QUIZ_INCORRECT = "/quiz_incorrect";
    public static final String QUIZ_START = "/quiz_start";
    public static final String QUIZ_END = "/quiz_end";
    //Храним варианты ответа

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
            if (lastAnswer.equals(QUIZ_START)){
                try {
                    return nextQuestion(user,message);
                }catch (QuizEmptyException q){
                    log.error("Quiz is empty");
                    return endQuiz(user);
                }
            }
        }
        return startNewQuiz(user, message);


    }





    private StringBuilder correctAnswer(CustomUser user, String message) {
        log.info("correct");
        userRepository.save(user);

        lastAnswer = QUIZ_START;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Your answer is correct");

        return stringBuilder;
    }

    private StringBuilder  incorrectAnswer(CustomUser user,String message) {

        userRepository.save(user);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Your answer is incorrect");

        lastAnswer = QUIZ_START;



        return stringBuilder;

    }

   private List<PartialBotApiMethod<? extends Serializable>> startNewQuiz(CustomUser user,String message) {
        user.setBotState(State.PLAYING_QUIZ);
        user = userService.save(user);
        lastAnswer = QUIZ_START;

        initQuiz(user);
        return nextQuestion(user,message);
    }

    private List<PartialBotApiMethod<? extends Serializable>> nextQuestion(CustomUser user,String message) {
        Word nextWord;

        if (quiz.getCurrentWord() == null){
            quiz.setCurrentWord(nextWord().orElseThrow(QuizEmptyException::new));
            quizService.saveQuiz(quiz);
        }
        if (message != null & Objects.equals(message, "/quiz_start")){
            SendMessage sendMessage = createMessageTemplate(user,"Word:\n-" + quiz.getCurrentWord().getTranslation());
            return List.of(sendMessage);
        }


        Word currentWord = quiz.getCurrentWord();
        String callbackData = "";


        StringBuilder  answer = new StringBuilder();
        if (message != null){
            callbackData = message.equals(currentWord.getValue()) ? QUIZ_CORRECT : QUIZ_INCORRECT;
            answer = new StringBuilder(
                    callbackData.equals(QUIZ_CORRECT) ? correctAnswer(user,message) : incorrectAnswer(user,message));
        }

        currentWord = nextWord().orElseThrow(QuizEmptyException::new);

        answer
                .append("\nWord:\n- ")
                .append(currentWord.getTranslation());
        SendMessage sendMessage = createMessageTemplate(user, answer.toString());
        return List.of(sendMessage);

    }

    private List<PartialBotApiMethod<? extends Serializable>> endQuiz(CustomUser user){
        user.setBotState(State.NONE);

        userService.save(user);
        System.out.println(user);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SendMessage sendMessage = createMessageTemplate(user,"end");
        return List.of(sendMessage);

    }



    private Optional<Word> nextWord(){
        if (!quiz.getQueue().isEmpty()){
            return Optional.ofNullable(quiz.poll());
        }
        return Optional.empty();
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
        return List.of(QUIZ_START, QUIZ_CORRECT, QUIZ_INCORRECT,QUIZ_END);
    }
}
