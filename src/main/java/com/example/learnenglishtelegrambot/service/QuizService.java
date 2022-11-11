package com.example.learnenglishtelegrambot.service;

import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.model.Quiz;
import com.example.learnenglishtelegrambot.model.Word;
import com.example.learnenglishtelegrambot.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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



    @Transactional
    public void deleteQuiz(CustomerUser customerUser){
        if (customerUser.getQuiz() != null){
            Optional<Quiz> quiz = quizRepository.findById(customerUser.getQuiz().getId());
            customerUser.setQuiz(null);
            quizRepository.delete(quiz.get());
            userService.save(customerUser);
        }

    }

    @Transactional
    public Quiz createQuiz(CustomerUser customerUser){


        Quiz quiz = new Quiz();

        Random random = new Random();
        Set<Word> quizWords = new HashSet<>();
        List<Word> words = new ArrayList<>();


        switch (quiz.getQUIZ_MODE()){
            case NEW -> words = wordService.getNewWords(customerUser);
            case NORMAL -> words = wordService.getAllWordsByUser(customerUser);
        }
        if (quiz.getQUIZ_SIZE() > words.size()){
            quiz.setQUIZ_SIZE(words.size());
            quizRepository.save(quiz);
        }
        while (quizWords.size() < quiz.getQUIZ_SIZE() &&
                quiz.getQUIZ_SIZE() <= words.size()){
            quizWords.add(words.get(random.nextInt(words.size())));

        }
        quizWords.stream()
                .map(Word::getId)
                .forEach(id ->
                        quiz.getWordIds().add(id)
                );

        quiz.setCustomerUser(customerUser);
        quizRepository.save(quiz);
        customerUser.setQuiz(quiz);
        userService.save(customerUser);
        log.info("----[quiz] was created and saved to bd");
        return quiz;
    }

    @Transactional
    public Quiz getQuiz(CustomerUser customerUser) {

        if (customerUser.getQuiz() != null){
            return quizRepository.findById(customerUser.getQuiz().getId()).get();
        }
        else return createQuiz(customerUser);

    }

}
