package com.example.learnenglishtelegrambot.repository;

import com.example.learnenglishtelegrambot.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.User;

@Repository
public interface UserRepository extends JpaRepository<CustomUser,Long> {

}
