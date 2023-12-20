package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Dictionary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository  extends CrudRepository<Dictionary, Long> {
    Dictionary findDictionaryById(Long id);
    void deleteDictionaryById(Long id);
}
