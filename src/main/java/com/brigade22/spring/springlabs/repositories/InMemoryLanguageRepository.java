package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Language;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryLanguageRepository implements LanguageRepository {
    private final List<Language> languages = new ArrayList<>();

    public InMemoryLanguageRepository() {}

    public Language save(Language language) {
        Language newLanguage = new Language(language.getCode(), language.getName());
        this.languages.add(newLanguage);

        return newLanguage;
    }

    public List<Language> findAll() {
        return this.languages;
    }

    public void delete(String code) {
        this.languages.removeIf((l) -> l.getCode().equals(code));
    }

    public Language deleteByCode(String code) {
        for (int i = 0; i < languages.size(); i++) {
            if (languages.get(i).getCode().equals(code)) {
                return this.languages.remove(i);
            }
        }

        return null;
    }

    public Language findByCode(String code) {
        for (Language language : languages) {
            if (language.getCode().equals(code)) {
                return language; // Return the language if the code matches
            }
        }
        return null; // Return null if no matching language is found
    }

    public Language findByName(String name) {
        for (Language language : languages) {
            if (language.getName().equals(name)) {
                return language;
            }
        }

        return null;
    }
}
