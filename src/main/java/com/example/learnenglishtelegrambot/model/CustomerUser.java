package com.example.learnenglishtelegrambot.model;

import com.example.learnenglishtelegrambot.telegram.enams.State;
import lombok.*;
import org.checkerframework.checker.units.qual.Temperature;

import javax.persistence.*;
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

    @Temperature
    private State botState;


}
