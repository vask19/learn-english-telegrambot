package com.example.learnenglishtelegrambot.repository;

import com.example.learnenglishtelegrambot.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
}
