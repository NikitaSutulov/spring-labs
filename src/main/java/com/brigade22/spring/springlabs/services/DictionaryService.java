package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.exceptions.ResourceNotFoundException;
import com.brigade22.spring.springlabs.controllers.requests.TranslationRequest;
import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.repositories.DictionaryRepository;
import com.brigade22.spring.springlabs.repositories.PostgresTranslationRepository;
import com.brigade22.spring.springlabs.repositories.PostgresWordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final PostgresTranslationRepository translationRepository;
    private final PostgresWordRepository wordRepository;

    public DictionaryService(DictionaryRepository dictionaryRepository, PostgresTranslationRepository translationRepository, PostgresWordRepository wordRepository) {
        this.dictionaryRepository = dictionaryRepository;
        this.translationRepository = translationRepository;
        this.wordRepository = wordRepository;
    }

    public Dictionary saveDictionary (Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    public List<Dictionary> getAll() {
        List<Dictionary> dictionaries = dictionaryRepository.findAll();

        for (Dictionary dictionary : dictionaries) {
            List<Translation> translations = translationRepository.findByDictionaryId(dictionary.getId());
            dictionary.setTranslations(translations);
        }

        return dictionaries;
    }
    public void deleteDictionaryById(int id) {
        dictionaryRepository.deleteById(id);
    }

    public Dictionary getDictionaryById(int id) {
        Dictionary dictionary = dictionaryRepository.findById(id);

        List<Translation> translations = translationRepository.findByDictionaryId(id);
        dictionary.setTranslations(translations);

        return dictionary;
    }

    public Dictionary getDictionaryByName(String name) {
        Dictionary dictionary = dictionaryRepository.findByName(name);

        List<Translation> translations = translationRepository.findByDictionaryId(dictionary.getId());
        dictionary.setTranslations(translations);

        return dictionary;
    }

    public Translation getTranslationForWord(int id, String word) {
        Dictionary dictionary = this.getDictionaryById(id);

        Optional<Translation> matchingTranslation = dictionary.getTranslations().stream()
                .filter(translation -> translation
                        .getWord()
                        .getValue()
                        .contentEquals(word))
                .findFirst();

        return matchingTranslation.orElse(null);
    }

    @Transactional
    public Translation addTranslation(int id, String word1, String word2) {
        Dictionary dictionary = this.getDictionaryById(id);
        Language language1 = dictionary.getLanguage1();
        Language language2 = dictionary.getLanguage2();

        Word word1Entity = wordRepository.save(new Word(language1, word1));
        Word word2Entity = wordRepository.save(new Word(language2, word2));

        Translation translation = new Translation(dictionary, word1Entity, word2Entity);
        return translationRepository.save(translation);
    }

    public Dictionary updateDictionary(int id, String name, Language language1, Language language2) {
        return dictionaryRepository.update(id, new Dictionary(id, name, language1, language2));
    }

    @Transactional
    public Translation updateTranslation(int translationId, TranslationRequest translationRequest) {
        Translation existingTranslation = translationRepository.findById(translationId);

        Word word = wordRepository.findById(existingTranslation.getWord().getId());
        Word translatedWord = wordRepository.findById(existingTranslation.getTranslatedWord().getId());

        if (word == null || translatedWord == null) {
            throw new ResourceNotFoundException("Translation with id " + translationId + " not found");
        }

        word.setValue(translationRequest.getWord());
        translatedWord.setValue(translationRequest.getTranslatedWord());

        wordRepository.update(word);
        wordRepository.update(translatedWord);

        return translationRepository.findById(translationId);
    }

    public Translation checkTranslation(int id, String word1, String word2) {
        Dictionary dictionary = getDictionaryById(id);
        for (Translation translation : dictionary.getTranslations()) {
            if (translation.getWord().getValue().equals(word1) && translation.getTranslatedWord().getValue().equals(word2)) {
                return translation;
            }
        }
        return null;
    }

    public void deleteTranslationById(int id) {
        Translation translation = translationRepository.findById(id);
        if (translation == null) {
            throw new ResourceNotFoundException("Translation with id " + id + " not found");
        }
        translationRepository.deleteTranslationById(id);
    }
}
