package com.example.learnenglishtelegrambot.utils;

import org.springframework.stereotype.Component;

@Component
public class AnswerDecorator {


    public static String decor(String value){
        return "<b>" + value + "</b>";
    }

    public static String decor(Integer value){
        return "<b>" + value + "</b>";

    }
}
