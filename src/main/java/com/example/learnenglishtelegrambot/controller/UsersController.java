package com.example.learnenglishtelegrambot.controller;

import com.example.learnenglishtelegrambot.model.User;
import com.example.learnenglishtelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${app.http.bot")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UsersController {

    private final UserService userService;

    /**
     * Возвращает список пользователей и связанных с ними планами
     */
    @RequestMapping(path = "/users_idea", method = RequestMethod.GET)
    public List<User> getIdeaList() {
        log.debug("Method - getIdeaList was called");
        return userService.getUserList();
    }
}
