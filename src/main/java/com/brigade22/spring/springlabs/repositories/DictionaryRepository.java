package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Dictionary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface DictionaryRepository {

    Dictionary save(Dictionary dictionary);

    List<Dictionary> findAll();

    void delete(Dictionary dictionary);
    Dictionary deleteById(long id);

    Dictionary findById(Long id);

    void clear();
    Dictionary update(Long id, Dictionary dictionary);
}
