//package com.example.learnenglishtelegrambot.handler;
//
//import com.example.learnenglishtelegrambot.domain.BotResponse;
//import com.example.learnenglishtelegrambot.telegram.enams.State;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
//
//import java.io.Serializable;
//import java.util.List;
//
//
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class SettingsHandler implements Handler{
//
//    private static final String SETTING_COMMAND = "/settings";
//    @Override
//    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {
//
//
//
//        return null;
//    }
//
//    @Override
//    public List<State> operatedBotState() {
//        return List.of(State.SETTINGS);
//    }
//
//    @Override
//    public List<String> operatedCallBackQuery() {
//        return List.of(SETTING_COMMAND);
//    }
//}
