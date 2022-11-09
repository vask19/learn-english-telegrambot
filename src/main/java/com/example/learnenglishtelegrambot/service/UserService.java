package com.example.learnenglishtelegrambot.service;

import com.example.learnenglishtelegrambot.model.CustomerUser;
import com.example.learnenglishtelegrambot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    private void saveUser(CustomerUser user){
        userRepository.save(user);
    }

    public List<CustomerUser> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public CustomerUser getUser(User user) {
        Long id = user.getId();
        return userRepository.findById(id).isPresent() ?
                userRepository.findById(id).get() :
               saveUser(id);
    }

    @Transactional
    public CustomerUser getUser(Long chatId) {

        return userRepository.findById(chatId).isPresent() ?
                userRepository.findById(chatId).get() :
                saveUser(chatId);
    }

    @Transactional
    public CustomerUser saveUser(Long id){;
        return userRepository.save(CustomerUser.builder()
                .id(id)
                .build());

    }

    @Transactional
    public CustomerUser save(CustomerUser user) {
        return userRepository.save(user);
    }
}
