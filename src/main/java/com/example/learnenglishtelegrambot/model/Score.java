package com.example.learnenglishtelegrambot.model;


import com.example.learnenglishtelegrambot.smiles.Icon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.learnenglishtelegrambot.utils.AnswerDecorator.decor;

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



    private int calculateCorrectAnswers(){
        int amount = correctAnswers.size() + incorrectAnswers.size();
        int percent =(100/amount) * correctAnswers.size();
//        if (amount % 2 != 0){
//            percent++;
//        }
        return percent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Your result:\n");
        int  percentResult = calculateCorrectAnswers();
        sb.append(String.format("%s%% correct answers %s\n\n",decor(percentResult), Icon.TROPHY.get()));
        sb.append("you had mistakes in next words:\n\n");
        int count = 1;
        for (Word word: incorrectAnswers){
            sb.append(decor(count++ + ") " + word.getValue()));
            sb.append(" = ");
            sb.append(decor(word.getTranslation()));
            sb.append("\n\n");

        }
        return sb.toString();
    }



}
