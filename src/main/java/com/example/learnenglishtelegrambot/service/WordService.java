package com.example.learnenglishtelegrambot.service;


import com.example.learnenglishtelegrambot.google.translateapi.Client;
import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.model.Word;
import com.example.learnenglishtelegrambot.repository.UserRepository;
import com.example.learnenglishtelegrambot.repository.WordRepository;
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


    public List<Word> getAllWordsByUser(CustomUser user){
        return wordRepository.findAllByUser(user);
    }


    @Transactional
    public Word saveWord(Word word){
        Optional<Word> wordInBd = wordRepository.findByValue(word.getValue());
        if (wordInBd.isPresent()){
            word = wordInBd.get();
            word.incrementPriority();
        }
        else {
            String translation = getTranslation(word.getValue());
            word.setTranslation(translation);
        }
        return wordRepository.save(word);

    }

    private String getTranslation(String value) {

        return client.translate(value);
    }

}
