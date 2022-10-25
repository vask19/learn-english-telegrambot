package com.example.learnenglishtelegrambot.telegram;
import com.example.learnenglishtelegrambot.domain.Quiz;
import com.example.learnenglishtelegrambot.google.translateapi.Client;
import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.model.Word;
import com.example.learnenglishtelegrambot.repository.UserRepository;
import com.example.learnenglishtelegrambot.service.UserService;
import com.example.learnenglishtelegrambot.service.WordService;
import com.example.learnenglishtelegrambot.telegram.enams.QuizMode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

@Slf4j
@Getter
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private Message requestMessage = new Message();
    private final SendMessage response = new SendMessage();
    private final UserService userService;
    private final WordService wordService;
    private final UserRepository userRepository;
    private final String botUsername;
    private final String botToken;
    private final TelegramBotsApi telegramBotsApi;
    private final Client client;



    public TelegramBot(UserService userService, WordService wordService, UserRepository userRepository, @Value("${telegram-bot.name}")
            String botUsername, @Value("${telegram-bot.token}") String botToken, TelegramBotsApi telegramBotsApi, Client client) throws TelegramApiException {
        this.userService = userService;
        this.wordService = wordService;
        this.userRepository = userRepository;
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.telegramBotsApi = telegramBotsApi;
        this.client = client;
        telegramBotsApi.registerBot(this);

    }


    /**
     * Этот метод вызывается при получении обновлений через метод GetUpdates.
     *
     * @param update Получено обновление
     */

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        String msgValue = msg.getText();
        Long usersId = user.getId();
        String answer = "";


        if (msgValue.contains("/")){
            switch (msgValue) {
                case "/all" -> answer = wordService.getWords(user);
                case "/5" -> {
                    System.out.println(1);
                    answer = wordService.getWords(user, 5);
                }
                case "/10" -> answer = wordService.getWords(user, 10);
                case "/20" -> answer = wordService.getWords(user, 20);
            }

            if (msgValue.equals("/quiz")){
                Quiz quiz = new Quiz(wordService);
                quiz.initQuiz(user);
                System.out.println(quiz.getQuizWords());
                answer = " eee";
            }



        }
        else {
            Word word = saveMessage(user,msg);
            answer = word.getValue() + ": " + word.getTranslation();
        }

        sendText(usersId,answer);




    }

    private Word saveMessage(User user, Message msg) {
        CustomUser customUser = userService.getUser(user);
        Word word = Word.builder()
                .user(customUser)
                .value(msg.getText().toLowerCase(Locale.ROOT))
                .build();
        return wordService.saveWord(word);


    }










    private void sendText(Long who,String what){
        SendMessage sm = SendMessage.builder()
                .parseMode("HTML")
                .chatId(who.toString())   //who are we sending a message to
                .text(what).build();      //message

        try {
            execute(sm);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }



    public void copyMessage(Long who,Integer msgId){
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())
                .chatId(who.toString())
                .messageId(msgId)
                .build();

        try {
            execute(cm);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }






}
