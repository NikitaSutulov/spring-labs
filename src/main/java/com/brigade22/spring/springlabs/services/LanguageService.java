package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.controllers.requests.DictionaryRequest;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.exceptions.ResourceNotFoundException;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Transactional
    public Language saveLanguage (Language language) {
        language.setCode(language.getCode().toLowerCase());
        return languageRepository.save(language);
    }

    public List<Language> getAll() {
        return languageRepository.findAll();
    }

    @Transactional
    public void deleteByCode(String code) {
        languageRepository.deleteLanguageByCode(code);
    }

    public Language findByCode(String code) {
        return languageRepository.findByCode(code); // Delegate to the repository's findByCode method
    }

    public Language findByName(String name) {
        return languageRepository.findLanguageByName(name);
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

    public void checkIfLanguagesExist(DictionaryRequest requestDictionary) {
        Language language1 = findByCode(requestDictionary.getLanguage1().getCode());
        Language language2 = findByCode(requestDictionary.getLanguage2().getCode());

        if (language1 == null || language2 == null) {
            throw new ResourceNotFoundException(
                    "Language not found"
            );
        }

        if (!Objects.equals(language1.getName(), requestDictionary.getLanguage1().getName())
                || !Objects.equals(language2.getName(), requestDictionary.getLanguage2().getName())) {
            throw new ResourceNotFoundException(
                    "Language with such name and code not found"
            );
        }
    }
}
