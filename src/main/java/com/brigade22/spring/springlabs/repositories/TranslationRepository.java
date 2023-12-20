package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Translation;
import org.springframework.data.repository.CrudRepository;

public interface TranslationRepository extends CrudRepository<Translation, Long> {
}

