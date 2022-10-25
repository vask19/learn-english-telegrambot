package com.example.learnenglishtelegrambot.repository;

import com.example.learnenglishtelegrambot.model.CustomUser;
import com.example.learnenglishtelegrambot.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word,Long> {
    Optional<Word> findByValue(String value);

    List<Word> findAllByUser(CustomUser user);

    @Query("SELECT w FROM Word w WHERE w.newWord = true and w.user = ?1")
    List<Word> getAllNewWordsByUser(CustomUser customUser);
//
//    @Query("SELECT w FROM Word w WHERE w. = true and w.user = ?1")
//    List<Word> getAllHardWordsByUser(CustomUser customUser);
}
