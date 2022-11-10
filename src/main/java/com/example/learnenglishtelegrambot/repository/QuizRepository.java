package com.example.learnenglishtelegrambot.repository;

import com.example.learnenglishtelegrambot.model.Quiz;
import com.example.learnenglishtelegrambot.model.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    Optional<Quiz> findByCustomerUser(CustomerUser customerUser);
}
