package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.controllers.requests.TranslationRequest;
import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.repositories.DictionaryRepository;
import com.brigade22.spring.springlabs.repositories.TranslationRepository;
import com.brigade22.spring.springlabs.repositories.WordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final WordRepository wordRepository;
    private final TranslationRepository translationRepository;

    @Autowired
    public DictionaryService(DictionaryRepository dictionaryRepository, WordRepository wordRepository, TranslationRepository translationRepository) {
        this.dictionaryRepository = dictionaryRepository;
        this.wordRepository = wordRepository;
        this.translationRepository = translationRepository;
    }

    public Dictionary saveDictionary (Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    public List<Dictionary> getAll() {
        List<Dictionary> dictionaries = new ArrayList<>();
        dictionaryRepository.findAll().forEach(dictionaries::add);
        return dictionaries;
    }

    @Transactional
    public void deleteDictionaryById(long id) {
        dictionaryRepository.deleteDictionaryById(id);
    }

    public Dictionary getDictionaryById(Long id) {
        return dictionaryRepository.findDictionaryById(id);
    }

    public Translation getTranslationForWord(Long id, String word) {
        Dictionary dictionary = this.getDictionaryById(id);

        Optional<Translation> matchingTranslation = dictionary.getTranslations().stream()
                .filter(translation -> translation.getWord().getValue().contentEquals(word))
                .findFirst();

        return matchingTranslation.orElse(null);
    }

    @Transactional
    public Translation addTranslation(Long id, String word1, String word2) {
        Dictionary dictionary = this.getDictionaryById(id);

        Word wordInLanguage1 = wordRepository.save(new Word(dictionary.getLanguage1(), word1));
        Word wordInLanguage2 = wordRepository.save(new Word(dictionary.getLanguage2(), word2));

        return translationRepository.save(new Translation(dictionary, wordInLanguage1, wordInLanguage2));
    }

    @Transactional
    public Dictionary updateDictionary(Long id, String name, Language language1, Language language2) {
        // Create a new dictionary object with the updated fields
        Dictionary updatedDictionary = new Dictionary(id, name, language1, language2);

        Dictionary existingDictionary = dictionaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dictionary not found with id: " + id));

        // Update fields of the existing dictionary
        existingDictionary.setName(updatedDictionary.getName());
        existingDictionary.setLanguage1(updatedDictionary.getLanguage1());
        existingDictionary.setLanguage2(updatedDictionary.getLanguage2());

        // Save the updated dictionary back to the database
        return dictionaryRepository.save(existingDictionary);
    }

    @Transactional
    public Translation updateTranslation(long translationId, TranslationRequest translationRequest) {
        Translation existingTranslation = translationRepository.findById(translationId)
                .orElseThrow(() -> new IllegalArgumentException("Translation not found"));


        Word existingWord = existingTranslation.getWord();
        Word existingTranslatedWord = existingTranslation.getTranslatedWord();

        existingWord.setValue(translationRequest.getWord());
        existingTranslatedWord.setValue(translationRequest.getTranslatedWord());

        wordRepository.save(existingWord);
        wordRepository.save(existingTranslatedWord);

        return translationRepository.save(existingTranslation);
    }

    @Transactional
    public void deleteTranslationById(Long translationId) {
        if (!translationRepository.existsById(translationId)) {
            throw new IllegalArgumentException("Translation not found");
        }

        translationRepository.deleteById(translationId);
    }
}
