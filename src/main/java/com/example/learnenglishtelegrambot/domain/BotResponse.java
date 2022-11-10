package com.example.learnenglishtelegrambot.domain;

import com.example.learnenglishtelegrambot.model.CustomerUser;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BotResponse {
    private Long from;
    private Long to;
    private String message;
    private CustomerUser user;

}
