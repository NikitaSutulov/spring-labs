package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.exceptions.ResourceNotFoundException;
import com.brigade22.spring.springlabs.entities.Language;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Primary
@Repository
@AllArgsConstructor
public class PostgresLanguageRepository implements LanguageRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final String FIND_ALL = "SELECT * FROM language";
    private static final String SAVE = "INSERT INTO language (code, name) VALUES (?, ?)";
    private static final String FIND_BY_CODE = "SELECT * FROM language WHERE code = ?";
    private static final String FIND_BY_NAME = "SELECT * FROM language WHERE name = ?";
    private static final String DELETE_BY_CODE = "DELETE FROM language WHERE code = ? RETURNING *";
    private static final String UPDATE_BY_CODE = "UPDATE language SET code = ?, name = ? WHERE code = ?";
    private static Language mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Language(rs.getString("code"), rs.getString("name"));
    }

    @Override
    public List<Language> findAll() {
        return jdbcTemplate.query(FIND_ALL, PostgresLanguageRepository::mapRow);
    }

    @Override
    public Language save(Language language) {
        jdbcTemplate.update(SAVE, language.getCode(), language.getName());
        return language;
    }

    @Override
    public Language findByCode(String code) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_CODE, PostgresLanguageRepository::mapRow, code);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResourceNotFoundException("Language with code " + code + " not found");
        }
    }

    @Override
    public Language findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_NAME, PostgresLanguageRepository::mapRow, name);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResourceNotFoundException("Language with name " + name + " not found");
        }
    }

    @Override
    public Language deleteByCode(String code) {
        try {
            return jdbcTemplate.queryForObject(DELETE_BY_CODE, PostgresLanguageRepository::mapRow, code);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Language with code " + code + " not found");
        }
    }

    @Override
    public Language updateLanguage(Language oldLanguage, Language newLanguage) {
        jdbcTemplate.update(UPDATE_BY_CODE, newLanguage.getCode(), newLanguage.getName(), oldLanguage.getCode());
        return findByCode(newLanguage.getCode());
    }
}
