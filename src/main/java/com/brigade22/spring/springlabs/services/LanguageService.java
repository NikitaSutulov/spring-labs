package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public void saveLanguage (Language language) {
        Language existing = languageRepository.findByCode(language.getCode().toLowerCase());

        if (existing != null) {
            return;
        }

        language.setCode(language.getCode().toLowerCase());
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



    public void updateLanguage(String code, Language language) {
        Language existing = languageRepository.findByCode(code);
        if (existing != null) {
            existing.setCode(language.getCode().toLowerCase());
            existing.setName(language.getName());
        } else {
            languageRepository.save(language);
        }
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
