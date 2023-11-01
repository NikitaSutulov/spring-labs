package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import jakarta.annotation.PostConstruct;
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

    public void saveLanguage (Language language) {
        languageRepository.save(language);
    }

    public List<Language> getAll() {
        return languageRepository.findAll();
    }

    public void deleteLanguage(String code) {
        languageRepository.delete(code);
    }

    public void deleteByCode(String code) {
        languageRepository.deleteByCode(code);
    }

    public Language findByCode(String code) {
        return languageRepository.findByCode(code); // Delegate to the repository's findByCode method
    }

    @PostConstruct
    public void initializeSampleData() {
        Language language1 = new Language("en", "English");
        Language language2 = new Language("ua", "Ukrainian");
        Language language3 = new Language("ru", "Russian");

        languageRepository.save(language1);
        languageRepository.save(language2);
        languageRepository.save(language3);
    }
}
