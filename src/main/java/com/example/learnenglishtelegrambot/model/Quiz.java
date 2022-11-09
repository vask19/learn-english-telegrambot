package com.example.learnenglishtelegrambot.model;

import com.example.learnenglishtelegrambot.telegram.enams.QuizMode;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Component
@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "users_id",referencedColumnName = "users_id")
    @ToString.Exclude
    private CustomerUser customerUser;
    @Builder.Default
    private int QUIZ_SIZE = 3;
    @Builder.Default
    private QuizMode QUIZ_MODE = QuizMode.NORMAL;

    @OneToMany(mappedBy = "quiz",fetch = FetchType.LAZY,cascade = CascadeType.REFRESH)
    private List<Word> words;





}
