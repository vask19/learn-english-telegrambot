package com.example.learnenglishtelegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    /**
     * user's id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;
    /**
     * user's name
     */
    private final String name;
    /**
     * description
     */
    private final String description;

    private String startWord = "";

    @Override
    public String toString() { return startWord + description; }
}
