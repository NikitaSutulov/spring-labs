package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.controllers.requests.TranslationRequest;
import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.repositories.DictionaryRepository;
import com.brigade22.spring.springlabs.repositories.TranslationRepository;
import com.brigade22.spring.springlabs.repositories.WordRepository;
import jakarta.annotation.PostConstruct;
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

    public Dictionary deleteDictionaryById(long id) {
        return dictionaryRepository.deleteById(id);
    }

    public Dictionary getDictionaryById(Long id) {
        return dictionaryRepository.findById(id);
    }

    public Word getTranslationForWord(Long id, String word) {
        List<Translation> translations = this.getDictionaryById(id).getTranslations();

        Optional<Word> matchingTranslation = translations.stream()
                .filter(translation -> translation
                        .getWord()
                        .getValue()
                        .contentEquals(word))
                .map(Translation::getTranslatedWord)
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

    public Dictionary updateDictionary(Long id, String name, Language language1, Language language2) {
        return dictionaryRepository.update(id, new Dictionary(id, name, language1, language2));
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
