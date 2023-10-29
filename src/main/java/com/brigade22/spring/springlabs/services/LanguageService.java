package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
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
