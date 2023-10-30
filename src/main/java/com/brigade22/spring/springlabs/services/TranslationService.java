package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.repositories.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslationService {

    // field injection
    @Autowired
    private TranslationRepository translationRepository;

    public void saveTranslation (Translation translation) {
        translationRepository.save(translation);
    }

    public List<Translation> getAll() {
        return translationRepository.findAll();
    }

    public void deleteTranslation(Word word, Word translation) {
        translationRepository.delete(word, translation);
    }
}
