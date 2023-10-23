package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Dictionary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DictionaryRepository {
    private final List<Dictionary> dictionaries = new ArrayList<>();

    public DictionaryRepository() {}

    public void save(Dictionary dictionary) {
        this.dictionaries.add(dictionary);
    }

    public List<Dictionary> findAll() {
        return this.dictionaries;
    }

    public void delete(String name) {
        this.dictionaries.removeIf((d) -> d.getName().equals(name));
    }
}
