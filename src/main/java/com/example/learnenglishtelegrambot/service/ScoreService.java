package com.example.learnenglishtelegrambot.service;

import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.model.Score;
import com.example.learnenglishtelegrambot.repository.ScoreRepository;
import com.example.learnenglishtelegrambot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;


    @Transactional
    public Score saveScore(Score score, CustomerUser user){
        score.setUser(user);
        return scoreRepository.save(score);






    }



}
