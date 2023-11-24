package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.repositories.DictionaryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryService(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    public void saveDictionary (Dictionary dictionary) {
        dictionaryRepository.save(dictionary);
    }

    public List<Dictionary> getAll() {
        return dictionaryRepository.findAll();
    }

    public void deleteDictionary(String name) {
        dictionaryRepository.delete(name);
    }

    public Dictionary getDictionaryByName(String name) {
        return dictionaryRepository.findByName(name);
    }

    public Word getTranslationForWord(String dictionaryName, String word) {
        List<Translation> translations = this.getDictionaryByName(dictionaryName).getTranslations();
        return translations.stream()
            .filter((translation -> translation
                .getWord()
                .getValue()
                .contentEquals(word)
            ))
            .toList()
            .get(0)
            .getTranslatedWord();
    }

    public void addTranslation(String dictionaryName, String word1, String word2) {
        Dictionary dictionary = this.getDictionaryByName(dictionaryName);
        Translation translation = new Translation(dictionary, new Word(dictionary.getLanguage1(), word1), new Word(dictionary.getLanguage2(), word2));
        dictionary.addTranslation(translation);
    }

    public void updateTranslation(String dictionaryName, String word1, String word2, String updatedWord1, String updatedWord2) {
        Dictionary dictionary = this.getDictionaryByName(dictionaryName);
        for (Translation translationSeek : dictionary.getTranslations()) {
            if (translationSeek.getWord().getValue().equals(word1) && translationSeek.getTranslatedWord().getValue().equals(word2)) {
                translationSeek.getWord().setValue(updatedWord1);
                translationSeek.getTranslatedWord().setValue(updatedWord2);
                break;
            }
        }
    }

    public Translation checkTranslation(String dictionaryName, String word1, String word2) {
        Dictionary dictionary = getDictionaryByName(dictionaryName);
        for (Translation translation : dictionary.getTranslations()) {
            if (translation.getWord().getValue().equals(word1) && translation.getTranslatedWord().getValue().equals(word2)) {
                return translation;
            }
        }
        return null;
    }

    @PostConstruct
    public void initializeSampleData() {
        Language language1 = new Language("en", "English");
        Language language2 = new Language("ua", "Ukrainian");
        Dictionary dictionary1 = new Dictionary("English-Ukrainian", language1, language2);
        Word word1 = new Word(language1, "Hello");
        Word word2 = new Word(language2, "Привіт");
        Translation translation1 = new Translation(dictionary1, word1, word2);
        Translation translation2 = new Translation(dictionary1, word1, word2);
        Translation translation3 = new Translation(dictionary1, word1, word2);
        Translation translation4 = new Translation(dictionary1, word1, word2);
        dictionary1.addTranslation(translation1);
        dictionary1.addTranslation(translation2);
        dictionary1.addTranslation(translation3);
        dictionary1.addTranslation(translation4);
        Language language3 = new Language("pl", "Polish");
        Dictionary dictionary2 = new Dictionary("Polish-Ukrainian", language3, language2);
        Dictionary dictionary3 = new Dictionary("English-Polish", language1, language3);

        dictionaryRepository.save(dictionary1);
        dictionaryRepository.save(dictionary2);
        dictionaryRepository.save(dictionary3);
    }
}
