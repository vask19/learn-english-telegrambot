package com.example.learnenglishtelegrambot.model;

import com.example.learnenglishtelegrambot.telegram.enams.QuizMode;
import lombok.Builder;

import javax.persistence.*;

//@Entity
public class BotSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settingId;

    @OneToOne

    @Builder.Default
    private static final int QUIZ_SIZE = 5;

    @Builder.Default
    private static final QuizMode QUIZ_MODE = QuizMode.NORMAL;



}
