package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Word;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Primary
@Repository
@AllArgsConstructor
public class PostgresWordRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String FIND_BY_ID = "SELECT w.id, w.value, w.language_code, l.name as language_name " +
            "FROM word w " +
            "JOIN language l ON w.language_code = l.code " +
            "WHERE w.id = ?";
    private static final String SAVE = "INSERT INTO word (value, language_code) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE word SET value = ?, language_code = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM word WHERE id = ?";
    private static final String FIND_BY_VALUE = "SELECT id, value, language_code FROM word WHERE value = ?";

    private static Word mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Word(
                rs.getInt("id"),
                new Language(rs.getString("language_code"), rs.getString("language_name")),
                rs.getString("value"));
    }

    public Word findById(int id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID, PostgresWordRepository::mapRow, id);
    }

    public Word findByValue(String value) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_VALUE, PostgresWordRepository::mapRow, value);
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no matching word is found
        }
    }

    public Word save(Word word) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, word.getValue());
            ps.setString(2, word.getLanguage().getCode());
            return ps;
        }, keyHolder);

        Map<String, Object> generatedKeys  = keyHolder.getKeys();

        if (generatedKeys != null && generatedKeys.containsKey("id")) {
            word.setId((Integer) generatedKeys.get("id"));
        }

        return word;
    }

    public Word update(Word word) {
        jdbcTemplate.update(UPDATE, word.getValue(), word.getLanguage().getCode(), word.getId());
        return findById(word.getId());
    }

    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }


}
