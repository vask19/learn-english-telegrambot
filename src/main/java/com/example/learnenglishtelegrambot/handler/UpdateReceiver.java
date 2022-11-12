package com.example.learnenglishtelegrambot.handler;

import com.example.learnenglishtelegrambot.domain.BotResponse;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.repository.UserRepository;
import com.example.learnenglishtelegrambot.service.UserService;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateReceiver {
    // Храним доступные хендлеры в списке (подсмотрел у Miroha)
    private final List<Handler> handlers;
    // Имеем доступ в базу пользователей
    private final UserRepository userRepository;
    private final UserService userService;






    private BotResponse createBotResponse(Update update){
      return BotResponse.builder()
                .from(update.getMessage().getFrom().getId())
                .to(update.getMessage().getChat().getId())
                .build();
    }

    // Обрабатываем полученный Update
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {

        BotResponse botResponse = createBotResponse(update);
        System.out.println(update.getMessage());
        Message message = update.getMessage();
        User user = update.getMessage().getFrom();
        CustomerUser customUser = userService.getUser(user);
        System.out.println(customUser.getBotState());
        botResponse.setUser(customUser);

        if (customUser.getBotState() == null){
            customUser.setBotState(State.NONE);
            userService.save(customUser);
        }

        if (update.getMessage().isCommand()){
            return getHandlerByCallBackQuery(update.getMessage().getText()).handle(botResponse,message.getText());
        }



                return getHandlerByState(customUser.getBotState()).handle(botResponse, message.getText());





    }

    private Handler getHandlerByState(State state) {
        handlers.forEach(System.out::println);
        return handlers.stream()

                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().contains(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        return handlers.stream()
                .filter(h -> h.operatedCallBackQuery().stream()
                        .anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
