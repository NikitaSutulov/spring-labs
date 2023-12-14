package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
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

@Repository
@AllArgsConstructor
public class PostgresTranslationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final DictionaryRepository dictionaryRepository;
    private final PostgresWordRepository wordRepository;

    private static final String SAVE = "INSERT INTO translation (dictionary_id, word_id, translated_word_id) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE translation SET word_id = ?, translated_word_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM translation WHERE id = ?";
    private static final String FIND_BY_DICTIONARY_ID = "SELECT t.id, t.dictionary_id, t.word_id, t.translated_word_id, " +
            "w1.value as word_value, l1.code as word_language_code, l1.name as word_language_name, " +
            "w2.value as translated_word_value, l2.code as translated_word_language_code, l2.name as translated_word_language_name " +
            "FROM translation t " +
            "JOIN word w1 ON t.word_id = w1.id " +
            "JOIN language l1 ON w1.language_code = l1.code " +
            "JOIN word w2 ON t.translated_word_id = w2.id " +
            "JOIN language l2 ON w2.language_code = l2.code " +
            "WHERE t.dictionary_id = ?";
    private static final String FIND_BY_ID = "SELECT id, dictionary_id, word_id, translated_word_id FROM translation WHERE id = ?";

    private Translation mapRow(ResultSet rs, int rowNum) throws SQLException {
        int translationId = rs.getInt("id");
        int dictionaryId = rs.getInt("dictionary_id");
        int wordId = rs.getInt("word_id");
        int translatedWordId = rs.getInt("translated_word_id");

        Dictionary dictionary = dictionaryRepository.findById(dictionaryId);
        Word word = wordRepository.findById(wordId);
        Word translatedWord = wordRepository.findById(translatedWordId);

        Translation translation = new Translation(dictionary, word, translatedWord);
        translation.setId(translationId);

        return translation;
    }

    public Translation save(Translation translation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, translation.getDictionary().getId());
            ps.setInt(2, translation.getWord().getId());
            ps.setInt(3, translation.getTranslatedWord().getId());
            return ps;
        }, keyHolder);

        int generatedId = (int) keyHolder.getKeys().get("id");
        translation.setId(generatedId);

        return translation;
    }

    public Translation update(int translationId, Translation updatedTranslation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, updatedTranslation.getWord().getId());
            ps.setInt(2, updatedTranslation.getTranslatedWord().getId());
            ps.setInt(3, translationId);
            return ps;
        }, keyHolder);

        if (!keyHolder.getKeyList().isEmpty()) {
            // Assuming only one key is generated
            int generatedId = (int) keyHolder.getKeys().get("id");
            updatedTranslation.setId(generatedId);
        }

        return updatedTranslation;
    }
    public Translation findById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID, this::mapRow, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResourceNotFoundException("Translation with id " + id + " not found");
        }
    }

    public List<Translation> findByDictionaryId(int dictionaryId) {
        try {
            return jdbcTemplate.query(FIND_BY_DICTIONARY_ID, this::mapRow, dictionaryId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResourceNotFoundException("Dictionary with id " + dictionaryId + " not found");
        }
    }

    public void deleteTranslationById(int dictionaryId) {
        jdbcTemplate.update(DELETE, dictionaryId);
    }
}

