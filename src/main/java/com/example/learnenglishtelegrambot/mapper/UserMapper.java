package com.example.learnenglishtelegrambot.mapper;

import com.example.learnenglishtelegrambot.model.CustomUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class UserMapper implements RowMapper<CustomUser> {

    @Override
    public CustomUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        var entity = new CustomUser();
        log.trace("mapRow(): entity = [{}]", entity);
        return entity;
    }
}
