package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.controllers.requests.DictionaryRequest;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import jakarta.annotation.PostConstruct;
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

    public Language saveLanguage (Language language) {
        language.setCode(language.getCode().toLowerCase());
        return languageRepository.save(language);
    }

    public List<Language> getAll() {
        return languageRepository.findAll();
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

    public void checkIfLanguageExists(DictionaryRequest requestDictionary) {
        Language possibleLanguage1 = findByName(requestDictionary.getLanguage1().getName());

        if (possibleLanguage1 != null && !Objects.equals(possibleLanguage1.getCode(), requestDictionary.getLanguage1().getCode())) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Language code is wrong"
            );
        }

        Language possibleLanguage2 = findByName(requestDictionary.getLanguage2().getName());

        if (possibleLanguage2 != null && !Objects.equals(possibleLanguage2.getCode(), requestDictionary.getLanguage2().getCode())) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Language code is wrong"
            );
        }

        if (findByCode(requestDictionary.getLanguage1().getCode()) == null) {
            saveLanguage(requestDictionary.getLanguage1());
        }
        Language language1 = findByCode(requestDictionary.getLanguage1().getCode());
        if (findByCode(requestDictionary.getLanguage2().getCode()) == null) {
            saveLanguage(requestDictionary.getLanguage2());
        }
        Language language2 = findByCode(requestDictionary.getLanguage2().getCode());

        if (!Objects.equals(requestDictionary.getLanguage1().getName(), language1.getName()) || !Objects.equals(requestDictionary.getLanguage2().getName(), language2.getName())) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Language name is wrong"
            );
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
