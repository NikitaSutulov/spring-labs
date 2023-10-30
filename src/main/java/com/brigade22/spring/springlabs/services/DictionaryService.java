package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.repositories.DictionaryRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryService(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    public void saveDictionary (Dictionary dictionary) {
        dictionaryRepository.save(dictionary);
    }

    public List<Dictionary> getAll() {
        return dictionaryRepository.findAll();
    }

    public void deleteDictionary(String name) {
        dictionaryRepository.delete(name);
    }
}
