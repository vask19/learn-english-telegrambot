package com.example.learnenglishtelegrambot.model;

import com.example.learnenglishtelegrambot.telegram.enams.QuizMode;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.REFRESH)
    private CustomerUser customerUser;
    @Builder.Default
    private int QUIZ_SIZE = 5;

    @Builder.Default
    private QuizMode QUIZ_MODE = QuizMode.NORMAL;


    @ToString.Exclude
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> wordIds = new ArrayList<>();





}
