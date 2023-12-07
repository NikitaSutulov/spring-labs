package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public Language saveLanguage (Language language) {
        language.setCode(language.getCode().toLowerCase());
        return languageRepository.save(language);
    }

    public List<Language> getAll() {
        return languageRepository.findAll();
    }

    public void deleteLanguage(String code) {
        languageRepository.delete(code);
    }

    public Language deleteByCode(String code) {
        return languageRepository.deleteByCode(code);
    }

    public Language findByCode(String code) {
        return languageRepository.findByCode(code); // Delegate to the repository's findByCode method
    }

    public Language findByName(String name) {
        return languageRepository.findByName(name);
    }

    public Language updateLanguage(String code, Language language) {
        Language existing = languageRepository.findByCode(code);
        if (existing != null) {
            existing.setCode(language.getCode().toLowerCase());
            existing.setName(language.getName());

            return existing;
        }

        return languageRepository.save(language);
    }

    @PostConstruct
    public void initializeSampleData() {
        Language language1 = new Language("en", "English");
        Language language2 = new Language("ua", "Ukrainian");
        Language language3 = new Language("pl", "Polish");

        languageRepository.save(language1);
        languageRepository.save(language2);
        languageRepository.save(language3);
    }
}
