package com.example.learnenglishtelegrambot.model;

import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
@Entity
@Table(name = "users")

public class CustomerUser {

    @Id
    @Column(name = "users_id")
    private Long id;


    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Word> words;

    private String name;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Score> scores;



    private State botState;

    @Override
    public String toString() {
        return "CustomerUser{" +
                "id=" + id +
                '}';
    }

    public void initScore() {
        scores = new ArrayList<>();
    }
}
