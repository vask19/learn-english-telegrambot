package com.example.learnenglishtelegrambot.service;

import com.example.learnenglishtelegrambot.domain.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class QuizService {



    public Quiz saveQuiz(Quiz quiz){

        return null;
    }

    public Quiz getQuiz() {
        return new Quiz();
    }
}
