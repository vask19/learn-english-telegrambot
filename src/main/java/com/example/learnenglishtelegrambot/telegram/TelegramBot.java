//package com.example.learnenglishtelegrambot.telegram;
//import com.example.learnenglishtelegrambot.model.Quiz;
//import com.example.learnenglishtelegrambot.google.translateapi.Client;
//import com.example.learnenglishtelegrambot.model.CustomerUser;
//import com.example.learnenglishtelegrambot.model.Word;
//import com.example.learnenglishtelegrambot.repository.UserRepository;
//import com.example.learnenglishtelegrambot.service.UserService;
//import com.example.learnenglishtelegrambot.service.WordService;
//import com.example.learnenglishtelegrambot.utils.AnswerDecorator;
//import lombok.Getter;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.User;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import java.util.Locale;
////
////@Slf4j
////@Getter
////@Component
//public class TelegramBot extends TelegramLongPollingBot {
//    private Message requestMessage = new Message();
//    private final SendMessage response = new SendMessage();
//    private final UserService userService;
//    private final WordService wordService;
//    private final UserRepository userRepository;
//    private final String botUsername;
//    private final String botToken;
//    private final TelegramBotsApi telegramBotsApi;
//    private final Client client;
//    private boolean quizRunning;
//    private Quiz quiz;
//    private final AnswerDecorator answerDecorator;
//
//
//
//    public TelegramBot(UserService userService, WordService wordService, UserRepository userRepository, @Value("${telegram-bot.name}")
//            String botUsername, @Value("${telegram-bot.token}") String botToken, TelegramBotsApi telegramBotsApi, Client client, AnswerDecorator answerDecorator) throws TelegramApiException {
//        this.userService = userService;
//        this.wordService = wordService;
//        this.userRepository = userRepository;
//        this.botUsername = botUsername;
//        this.botToken = botToken;
//        this.telegramBotsApi = telegramBotsApi;
//        this.client = client;
//        this.answerDecorator = answerDecorator;
//        telegramBotsApi.registerBot(this);
//
//    }
//
//
//    /**
//     * ???????? ?????????? ???????????????????? ?????? ?????????????????? ???????????????????? ?????????? ?????????? GetUpdates.
//     *
//     * @param update ???????????????? ????????????????????
//     */
//
//    @SneakyThrows
//    @Override
//    public void onUpdateReceived(Update update) {
//        var msg = update.getMessage();
//        var user = msg.getFrom();
//        String msgValue = msg.getText();
//        Long usersId = user.getId();
//        String answer = "";
//
//
//
//        if (quizRunning){
//            check(usersId,msgValue);
//            nextWord(usersId);
//        }
//
//        else if (msg.isCommand()){
//
//            switch (msgValue) {
//                case "/all" -> answer = wordService.getWords(user);
//                case "/5" -> {
//                    answer = wordService.getWords(user, 5);
//                }
//                case "/10" -> answer = wordService.getWords(user, 10);
//                case "/20" -> answer = wordService.getWords(user, 20);
//            }
//
//            if (msgValue.equals("/quiz")){
//                if (quizRunning) return;
//                quiz = new Quiz(wordService,user);
//                quiz.initQuiz();
//                quizRunning = true;
//                nextWord(usersId);
//
//            }
//        }
//        else {
//            Word word = saveMessage(user,msg);
//            answer = word.getValue() + ": " + word.getTranslation();
//        }
//
//        if (!answer.isEmpty()){
//            sendText(usersId,answer);
//        }
//
//
//
//
//    }
//
//    private void nextWord(Long userId){
//        if (quiz.end()) {
//            quizRunning = false;
//            return;
//        }
//        Word word = quiz.next();
//        sendText(userId,word.getTranslation());
//
//    }
//
//    private void check(Long userId,String answer){
//        if (answer.equals("/stop")){
//            quizRunning = false;
//            quiz.stop();
//        }
//        else {
//            Word word = quiz.poll();
//            sendText(userId,createAnswer(answer,word));
//
//        }
//
//    }
//
//    private String createAnswer(String answer,Word word){
//        String wordValue = word.getValue();
//        StringBuilder sb = new StringBuilder("Your answer ");
//        sb.append(answer.equals(wordValue) ? "<b>is correct</b>" : "<b>isn't correct</b>");
//        sb.append("\n");
//        sb.append("your answer: ").append(answerDecorator.decor(answer)).append("\n");
//        sb.append("correct answer: ").append(answerDecorator.decor(wordValue)).append("\n");
//        return sb.toString();
//
//
//
//    }
//
//    private Word saveMessage(User user, Message msg) {
//        CustomerUser customUser = userService.getUser(user);
//        Word word = Word.builder()
//                .user(customUser)
//                .value(msg.getText().toLowerCase(Locale.ROOT))
//                .build();
//        return wordService.saveWord(word);
//
//
//    }
//
//
//
//
//
//
//
//
//
//
//    private void sendText(Long who,String what){
//        SendMessage sm = SendMessage.builder()
//                .parseMode("HTML")
//                .chatId(who.toString())   //who are we sending a message to
//                .text(what).build();      //message
//
//        try {
//            execute(sm);
//        }catch (TelegramApiException e){
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//    }
//
//
//
//
//
//
//
