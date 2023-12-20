package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Word;
import org.springframework.data.repository.CrudRepository;

public interface WordRepository extends CrudRepository<Word, Long> {
}