package com.example.learnenglishtelegrambot.service;

import com.example.learnenglishtelegrambot.model.Quiz;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.model.Word;
import com.example.learnenglishtelegrambot.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j

public class QuizService {
    private final QuizRepository quizRepository;
    private final UserService userService;
    private final WordService wordService;



    public Quiz saveQuiz(Quiz quiz){

        return null;
    }


    public Quiz createQuiz(CustomerUser customerUser){
        Quiz quiz = quizRepository.findByCustomerUser(customerUser).orElse(new Quiz());
        Random random = new Random();
        Set<Word> quizWords = new HashSet<>();
        List<Word> words = new ArrayList<>();


        switch (quiz.getQUIZ_MODE()){
            case NEW -> words = wordService.getNewWords(customerUser);
            case NORMAL -> words = wordService.getAllWordsByUser(customerUser);
        }
        while (quizWords.size() < quiz.getQUIZ_SIZE() & quiz.getQUIZ_SIZE() <= words.size()){
            quizWords.add(words.get(random.nextInt(words.size())));

        }
        quiz.setWords(words);
        quizRepository.save(quiz);
        log.info("----[quiz] was created and saved to bd");
        return quiz;
    }

    public Quiz getQuiz() {
        return new Quiz();
    }
}
