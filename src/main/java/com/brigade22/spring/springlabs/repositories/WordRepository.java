package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Word;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WordRepository {
    private List<Word> words = new ArrayList<>();

    public WordRepository() {}

    public void save(Word word) {
        this.words.add(word);
    }

    public List<Word> findAll() {
        return this.words;
    }

    public void delete(String value) {
        this.words.removeIf((w) -> w.getValue().equals(value));
    }
}
