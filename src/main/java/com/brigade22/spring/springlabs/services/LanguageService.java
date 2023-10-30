package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private LanguageRepository languageRepository;

    // setter
    @Autowired
    public void setLanguageRepository(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public void saveDictionary (Language language) {
        languageRepository.save(language);
    }

    public List<Language> getAll() {
        return languageRepository.findAll();
    }

    public void deleteDictionary(String code) {
        languageRepository.delete(code);
    }
}
