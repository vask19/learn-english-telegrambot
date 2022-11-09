package com.example.learnenglishtelegrambot.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BotResponse {
    private Long from;
    private Long to;
    private String message;

}
