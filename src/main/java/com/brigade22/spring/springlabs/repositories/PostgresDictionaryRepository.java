package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.exceptions.ResourceNotFoundException;
import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Language;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Primary
@Repository
@AllArgsConstructor
public class PostgresDictionaryRepository implements DictionaryRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL = "SELECT d.id, d.name, d.language1_code, l1.name as language1_name, d.language2_code, l2.name as language2_name " +
            "FROM dictionary d " +
            "JOIN language l1 ON d.language1_code = l1.code " +
            "JOIN language l2 ON d.language2_code = l2.code";
    private static final String SAVE = "INSERT INTO dictionary (name, language1_code, language2_code) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT d.id, d.name, d.language1_code, l1.name as language1_name, d.language2_code, l2.name as language2_name " +
            "FROM dictionary d " +
            "JOIN language l1 ON d.language1_code = l1.code " +
            "JOIN language l2 ON d.language2_code = l2.code " +
            "WHERE d.id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM dictionary WHERE id = ?";
    private static final String UPDATE_BY_ID = "UPDATE dictionary SET name = ?, language1_code = ?, language2_code = ? WHERE id = ?";
    private static final String FIND_BY_NAME = "SELECT d.id, d.name, d.language1_code, l1.name as language1_name, d.language2_code, l2.name as language2_name " +
            "FROM dictionary d " +
            "JOIN language l1 ON d.language1_code = l1.code " +
            "JOIN language l2 ON d.language2_code = l2.code " +
            "WHERE d.name = ?";

    private static Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Dictionary(
                rs.getInt("id"),
                rs.getString("name"),
                new Language(rs.getString("language1_code"), rs.getString("language1_name")),
                new Language(rs.getString("language2_code"), rs.getString("language2_name"))
        );
    }
    @Override
    public List<Dictionary> findAll() {
        return jdbcTemplate.query(FIND_ALL, PostgresDictionaryRepository::mapRow);
    }

    @Override
    public Dictionary save(Dictionary dictionary) {
        Object[] params = { dictionary.getName(), dictionary.getLanguage1().getCode(), dictionary.getLanguage2().getCode() };
        executeUpdateAndExtractGeneratedKey(SAVE, params, dictionary);

        return dictionary;
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public Dictionary findById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID, PostgresDictionaryRepository::mapRow, id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Dictionary with id " + id + " not found");
        }
    }

    @Override
    public Dictionary update(int id, Dictionary dictionary) {
        executeUpdateAndExtractGeneratedKey(UPDATE_BY_ID, new Object[] { dictionary.getName(), dictionary.getLanguage1().getCode(), dictionary.getLanguage2().getCode(), id }, dictionary);
        return findById(id);
    }

    @Override
    public Dictionary findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_NAME, PostgresDictionaryRepository::mapRow, name);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResourceNotFoundException("Dictionary with name " + name + " not found");
        }
    }

    private void executeUpdateAndExtractGeneratedKey(String sql, Object[] params, Dictionary dictionary) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps;
        }, keyHolder);

        Map<String, Object> generatedKeys = keyHolder.getKeys();

        if (generatedKeys != null && !generatedKeys.isEmpty()) {
            int generatedId = (int) generatedKeys.get("id");
            dictionary.setId(generatedId);
        }
    }
}
