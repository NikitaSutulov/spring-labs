package com.brigade22.spring.springlabs.services;

import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.repositories.DictionaryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @PostConstruct
    public void initializeSampleData() {
        Dictionary dictionary1 = new Dictionary("English-Ukrainian");
        Language language1 = new Language("en", "English");
        Language language2 = new Language("ua", "Ukrainian");
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
        Dictionary dictionary2 = new Dictionary("Polish-Ukrainian");
        Dictionary dictionary3 = new Dictionary("English-Polish");

        dictionaryRepository.save(dictionary1);
        dictionaryRepository.save(dictionary2);
        dictionaryRepository.save(dictionary3);
    }
}
