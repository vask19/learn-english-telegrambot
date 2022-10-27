package com.example.learnenglishtelegrambot.utils;

import org.springframework.stereotype.Component;

@Component
public class AnswerDecorator {


    public String decor(String value){
        return "<b>" + value + "</b>";
    }

    public String decor(Integer value){
        return "<b>" + value + "</b>";

    }
}
