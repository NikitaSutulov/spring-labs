package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Language;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository {
    Language save(Language language);
    List<Language> findAll();
    Language deleteByCode(String code);
    Language findByCode(String code);
    Language findByName(String name);
}
