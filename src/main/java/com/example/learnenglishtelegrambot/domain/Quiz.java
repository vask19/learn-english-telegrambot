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
    private Queue<Word> queue = new LinkedList<>();
    private static final int QUIZ_SIZE = 5;
    private static final QuizMode QUIZ_MODE = QuizMode.NORMAL;
    private  WordService wordService;
    private User user;

    public Quiz(WordService wordService, User user) {
        this.wordService = wordService;
        this.user = user;
    }


    //TODO : abstract fabric
    public void initQuiz(){
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

        queue.addAll(quizWords);



    }

    public Word next(){

        return queue.peek();
    }


    public void stop() {
    }




    public Word poll() {
        return queue.poll();
    }

    public boolean end() {
        return queue.isEmpty();
    }
}
