package com.example.learnenglishtelegrambot.repository;

import com.example.learnenglishtelegrambot.exceptions.DbException;
import com.example.learnenglishtelegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Integer, User> {

    /**
     * Возвращает список записей по id
     *
     * @return запрашиваемая запись
     * @throws DbException в случае ошибки БД
     */
    User getById(int id) throws DbException;

    /**
     * Возвращает список записей
     *
     * @return список всех записей
     * @throws DbException в случае ошибки БД
     */
    List<User> getUserList();

    /**
     * Вставка новой записи
     *
     * @param entity новая запись
     * @throws DbException в случае ошибки БД
     */
    void insert(User entity);

    /**
     * Удаление записи
     *
     * @param entity удаляемая запись
     * @throws DbException в случае ошибки БД
     */
    void delete(User entity);
}
