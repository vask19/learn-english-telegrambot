package com.example.learnenglishtelegrambot.domain;

import com.example.learnenglishtelegrambot.model.CustomerUser;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
public class BotResponse {
    private Long from;
    private Long to;
    private String message;
    @ToString.Exclude
    private CustomerUser user;

}
