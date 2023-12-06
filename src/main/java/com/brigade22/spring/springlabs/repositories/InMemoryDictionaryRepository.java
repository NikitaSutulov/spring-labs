package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Dictionary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class InMemoryDictionaryRepository implements DictionaryRepository {
    private final HashMap<Long, Dictionary> dictionaries = new HashMap<>();
    private long nextId = 1L;

    public InMemoryDictionaryRepository() {}

    public Dictionary save(Dictionary dictionary) {
        long id = nextId++;

        Dictionary newDictionary = new Dictionary(
                id,
                dictionary.getName(),
                dictionary.getLanguage1(),
                dictionary.getLanguage2()
        );
        newDictionary.setTranslations(dictionary.getTranslations());
        this.dictionaries.put(id, newDictionary);
        return newDictionary;
    }

    public List<Dictionary> findAll() {
        return new ArrayList<>(dictionaries.values());
    }

    public void delete(Dictionary dictionary) {
        this.dictionaries.remove(dictionary.getId());
    }

    public Dictionary deleteById(long id) {
        return this.dictionaries.remove(id);
    }

    public Dictionary findById(Long id) {
        return this.dictionaries.get(id);
    }

    public void clear() {
        this.dictionaries.clear();
    }

    public Dictionary update(Long id, Dictionary dictionary) {
        Dictionary updatedDictionary = new Dictionary(
                id,
                dictionary.getName(),
                dictionary.getLanguage1(),
                dictionary.getLanguage2()
        );
        updatedDictionary.setTranslations(dictionaries.get(id).getTranslations());
        System.out.println(id);
        this.dictionaries.replace(id, updatedDictionary);
        return updatedDictionary;
    }
}
