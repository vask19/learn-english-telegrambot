package com.example.learnenglishtelegrambot.domain;

import com.example.learnenglishtelegrambot.model.Word;
import com.example.learnenglishtelegrambot.service.WordService;
import com.example.learnenglishtelegrambot.telegram.enams.QuizMode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;

@Component
@Setter
@Getter
@RequiredArgsConstructor
public class Quiz {
    private Long id;
    private Set<Word> quizWords;
    private static final int QUIZ_SIZE = 5;
    private static final QuizMode QUIZ_MODE = QuizMode.NORMAL;
    private final WordService wordService;





//TODO : abstract fabric
    public void initQuiz(User user){
        Random random = new Random();
        quizWords = new HashSet<>();
        List<Word> words = new ArrayList<>();

        switch (QUIZ_MODE){
            case NEW -> words = wordService.getNewWords(user);
            case NORMAL -> words = wordService.getAllWordsByUser(user);


        }
        while (quizWords.size() < QUIZ_SIZE){
            quizWords.add(words.get(random.nextInt(words.size())));

        }



    }

    public Word next(){
        return null;
    }






}
