package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.controllers.requests.DictionaryRequest;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.exceptions.ResourceNotFoundException;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Transactional
    public Language saveLanguage (Language language) {
        language.setCode(language.getCode().toLowerCase());
        return languageRepository.save(language);
    }

    public List<Language> getAll() {
        List<Language> languages = new ArrayList<>();
        languageRepository.findAll().forEach(languages::add);
        return languages;
    }

    @Transactional
    public void deleteByCode(String code) {
        languageRepository.deleteLanguageByCode(code);
    }

    public Language findByCode(String code) {
        TypedQuery<Language> query = entityManager.createNamedQuery("Language.findByCode", Language.class);
        query.setParameter("code", code);
        List<Language> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public Language findByName(String name) {
        return languageRepository.findLanguageByName(name);
    }

    @Transactional
    public Language updateLanguage(String code, Language language) {
        Language existingLanguage = findByCode(code);

        if (existingLanguage != null) {
            languageRepository.updateLanguageByCode(code, language.getCode(), language.getName());
            return new Language(language.getCode(), language.getName());
        }

        return null;
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
