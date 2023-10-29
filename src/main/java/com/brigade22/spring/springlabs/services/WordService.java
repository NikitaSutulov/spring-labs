package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.repositories.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public void save(Word word) {
        wordRepository.save(word);
    }

    public List<Word> getAll() {
        return wordRepository.findAll();
    }

    public void delete(String value) {
        wordRepository.delete(value);
    }
}
