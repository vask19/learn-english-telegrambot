package com.example.learnenglishtelegrambot.repository;

import com.example.learnenglishtelegrambot.model.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<CustomerUser,Long> {

}
