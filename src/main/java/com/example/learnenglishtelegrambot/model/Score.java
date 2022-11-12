package com.example.learnenglishtelegrambot.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "score")
public class Score {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne()
    private CustomerUser user;

    @ManyToMany()
    @JoinTable(name = "scores_correct_answers",
        joinColumns = @JoinColumn(name = "score_id"),
        inverseJoinColumns = @JoinColumn(name = "word_id"))
    private List<Word> correctAnswers;

    @ManyToMany()
    @JoinTable(name = "scores_incorrect_answers",
        joinColumns = @JoinColumn(name = "score_id"),
        inverseJoinColumns = @JoinColumn(name = "word_id"))
    private List<Word> incorrectAnswers;

    public void initWordLists(){
        incorrectAnswers = new ArrayList<>();
        correctAnswers  = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                '}';
    }
}
