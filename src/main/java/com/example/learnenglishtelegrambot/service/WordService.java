package com.example.learnenglishtelegrambot.service;

import com.example.learnenglishtelegrambot.google.translateapi.Client;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.model.Word;
import com.example.learnenglishtelegrambot.repository.UserRepository;
import com.example.learnenglishtelegrambot.repository.WordRepository;
import com.example.learnenglishtelegrambot.utils.AnswerDecorator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final Client client;
    private final UserService userService;
    private final AnswerDecorator answerDecorator;

    public List<Word> getAllWordsByUser(CustomerUser user){

        return wordRepository.findAllByUser(user);
    }


    @Transactional
    public Word saveWord(Word word){
        Optional<Word> wordInBd = wordRepository.findByValue(word.getValue());
        if (wordInBd.isPresent()){
            word = wordInBd.get();
            word.moreDifficult();
        }
        else {
            String translation = getTranslation(word.getValue());
            word.setTranslation(translation);
        }
        return wordRepository.save(word);

    }

    @Transactional
    public Word updateWord(Word word){
        word.setNewWord(false);
        return wordRepository.save(word);
    }

    private String getTranslation(String value) {

        return client.translate(value);
    }


    public String getWords(CustomerUser user){
        List<Word> words = getAllWordsByUser(user);
        return createStringForAnswer(words).toString();

    }

    public String getWords(CustomerUser user, Integer amount) {
        List<Word> words = getAllWordsByUser(user);
        if (amount >= words.size()){
            amount = words.size();
        }
        List<Word> subList = words.subList(words.size() -amount,words.size());
        return createStringForAnswer(subList).toString();
    }



    private StringBuilder createStringForAnswer(List<Word> words){
        StringBuilder sb = new StringBuilder();
        WordCounter wordCounter = new WordCounter();
        words.forEach(word
                        -> {
                    sb
                            .append(answerDecorator.decor(wordCounter.getCurrentCount()))
                            .append(word.getValue())
                            .append(" : ")
                            .append(word.getTranslation())
                            .append("\n");
                }
        );

        return sb;
    }

    public List<Word> getNewWords(CustomerUser user) {
        List<Word> words = wordRepository.getAllNewWordsByUser(user);
        return words;
    }


    private class WordCounter{
        private int count;
        private int getCurrentCount(){
            return ++count;
        }
    }


}
