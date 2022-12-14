package com.example.learnenglishtelegrambot.model;

import lombok.*;

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

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.MERGE)
    private CustomerUser user;





    private String value;

    private String translation;

    private short difficult;
    private short priority;
    @Builder.Default
    private boolean newWord = true;


    public void moreDifficult(){

        if (difficult<5){
            difficult++;

        }
    }


    public void incrementPriority(){

        if (priority <3){
            priority++;

        }
    }

}
