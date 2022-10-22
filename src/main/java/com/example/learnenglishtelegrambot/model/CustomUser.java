package com.example.learnenglishtelegrambot.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class CustomUser {

    @Id
    @Column(name = "users_id")
    private Long id;


    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Word> words;

}
