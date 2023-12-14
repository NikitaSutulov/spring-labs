package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Language;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository {
    List<Language> findAll();
    Language save(Language language);
    Language findByCode(String code);
    Language findByName(String name);
    Language deleteByCode(String code);
    Language updateLanguage(Language oldLanguage, Language newLanguage);
}
