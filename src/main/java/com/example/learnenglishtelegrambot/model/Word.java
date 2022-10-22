package com.example.learnenglishtelegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private CustomUser user;

    private String value;

    private String translation;

    private short priority;
    @Builder.Default
    private boolean newWord = true;


    public void incrementPriority(){
        priority = priority<10 ? priority++ : priority;
    }

}