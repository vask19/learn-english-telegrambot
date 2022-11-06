package com.example.learnenglishtelegrambot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)

public class QuizEmptyException extends BaseException {
    private final static String MESSAGE = "Not Found";

    public QuizEmptyException(Throwable t) {
        super(MESSAGE, t);
    }

    public QuizEmptyException(String msg) {
        super(msg);
    }
    public QuizEmptyException() {
        super(MESSAGE);
    }
}
