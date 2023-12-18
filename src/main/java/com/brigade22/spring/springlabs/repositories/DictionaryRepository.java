package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Dictionary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryRepository {

    Dictionary save(Dictionary dictionary);

    List<Dictionary> findAll();
    void deleteById(int id);

    Dictionary findById(int id);

    Dictionary update(int id, Dictionary dictionary);

    Dictionary findByName(String name);
}
