package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.controllers.requests.DictionaryRequest;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.exceptions.ResourceNotFoundException;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import org.springframework.http.HttpStatus;
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
        validateUniqueCode(language.getCode());
        validateUniqueName(language.getName());

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

    public Language updateLanguage(Language existingLanguage, Language newLanguage) {
        validateCodeAndNameForUpdate(existingLanguage, newLanguage);

        return languageRepository.updateLanguage(existingLanguage, newLanguage);
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

    private void validateCodeAndNameForUpdate(Language existingLanguage, Language updatedLanguage) {
        // If code is changed, check if the new code is available
        if (!existingLanguage.getCode().equals(updatedLanguage.getCode())) {
            validateUniqueCode(updatedLanguage.getCode());
        }

        // If name is changed, check if the new name is available
        if (!existingLanguage.getName().equals(updatedLanguage.getName())) {
            validateUniqueName(updatedLanguage.getName());
        }
    }

    private void validateUniqueCode(String code) {
        try {
            if (findByCode(code) != null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Language with such a code already exists"
                );
            }
        } catch (ResourceNotFoundException ignored) {}
    }

    private void validateUniqueName(String name) {
        try {
            if (findByName(name) != null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Language with such a name already exists"
                );
            }
        } catch (ResourceNotFoundException ignored) {}
    }

}
