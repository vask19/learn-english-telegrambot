package com.example.learnenglishtelegrambot.repository;

import com.example.learnenglishtelegrambot.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Long> {
}
