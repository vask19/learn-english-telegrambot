package com.example.learnenglishtelegrambot.domain;

import com.example.learnenglishtelegrambot.model.Word;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Queue;

@Component
@Setter
@Getter
@RequiredArgsConstructor
public class Quiz {
    private Long id;
    private Queue<Word> queue;
    private User user;


    public Word next(){

        return queue.peek();
    }

    public void stop() {
    }




    public Word poll() {
        return queue.poll();
    }

    public boolean end() {
        return queue.isEmpty();
    }

}
