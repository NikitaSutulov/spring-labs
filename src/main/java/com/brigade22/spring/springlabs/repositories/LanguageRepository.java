package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Language;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LanguageRepository {
    private final List<Language> languages = new ArrayList<>();

    public LanguageRepository() {}

    public void save(Language language) {
        this.languages.add(language);
    }

    public List<Language> findAll() {
        return this.languages;
    }

    public void delete(String code) {
        this.languages.removeIf((l) -> l.getCode().equals(code));
    }

    public void deleteByCode(String code) {
        languages.removeIf(language -> language.getCode().equals(code));
    }

    public Language findByCode(String code) {
        for (Language language : languages) {
            if (language.getCode().equals(code)) {
                return language; // Return the language if the code matches
            }
        }
        return null; // Return null if no matching language is found
    }
}
