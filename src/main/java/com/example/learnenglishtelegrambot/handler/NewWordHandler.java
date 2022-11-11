package com.example.learnenglishtelegrambot.handler;


import com.example.learnenglishtelegrambot.domain.BotResponse;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.model.Word;
import com.example.learnenglishtelegrambot.service.UserService;
import com.example.learnenglishtelegrambot.service.WordService;
import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static com.example.learnenglishtelegrambot.util.TelegramUtil.createMessageTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewWordHandler implements Handler{

    private static final String NEW_WORD_COMMAND = "/new";
    private final UserService userService;
    private final WordService wordService;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(BotResponse botResponse, String message) {
        CustomerUser user = botResponse.getUser();
        user.setBotState(State.NEW_WORD);
        userService.save(user);


        return saveWord(botResponse,message);

    }

    public List<PartialBotApiMethod<? extends Serializable>> saveWord(BotResponse botResponse, String message) {
        String enterWordText = "" +
                "Enter your new word please: ";
        botResponse.setMessage(enterWordText);
        SendMessage enterWordMessage = createMessageTemplate(botResponse);
        if (message.equals("/new")){
            return List.of(enterWordMessage);

        }
        Word word = saveMessage(botResponse.getUser(),message);
        String yourWordWasText = String.format("" +
                "Your word is: %s\nTranslation: %s",word.getValue(),word.getTranslation());
        botResponse.setMessage(yourWordWasText);
        SendMessage yourWordWasMessage = createMessageTemplate(botResponse);

        return List.of(yourWordWasMessage,enterWordMessage);



    }




        private Word saveMessage(CustomerUser customerUser, String  msg) {
        Word word = Word.builder()
                .user(customerUser)
                .value(msg.toLowerCase(Locale.ROOT))
                .build();
        return wordService.saveWord(word);

    }


    @Override
    public List<State> operatedBotState() {
        return List.of(State.NEW_WORD);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(NEW_WORD_COMMAND);
    }
}
