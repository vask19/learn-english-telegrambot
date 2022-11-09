package com.example.learnenglishtelegrambot.mapper;

import com.example.learnenglishtelegrambot.model.CustomerUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class UserMapper implements RowMapper<CustomerUser> {

    @Override
    public CustomerUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        var entity = new CustomerUser();
        log.trace("mapRow(): entity = [{}]", entity);
        return entity;
    }
}
