package com.example.learnenglishtelegrambot.service;

import com.example.learnenglishtelegrambot.model.CustomUser;
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


    private void saveUser(CustomUser user){
        userRepository.save(user);
    }

    public List<CustomUser> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public CustomUser getUser(User user) {
        Long id = user.getId();
        CustomUser customUser =  userRepository.findById(id).isPresent() ?
                userRepository.findById(id).get() :
                CustomUser.builder()
                        .id(id)
                        .build();
        System.out.println(customUser);
        return userRepository.save(customUser);
    }
}
