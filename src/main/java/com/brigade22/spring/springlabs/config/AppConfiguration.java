package com.brigade22.spring.springlabs.config;

import com.brigade22.spring.springlabs.repositories.DictionaryRepository;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import com.brigade22.spring.springlabs.repositories.TranslationRepository;
import com.brigade22.spring.springlabs.repositories.WordRepository;
import com.brigade22.spring.springlabs.services.DictionaryService;
import com.brigade22.spring.springlabs.services.LanguageService;
import com.brigade22.spring.springlabs.services.WordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfiguration {

    @Bean
    @Scope("prototype")
    public DictionaryService dictionaryService(DictionaryRepository dictionaryRepository) {
        return new DictionaryService(dictionaryRepository);
    }

    @Bean
    @Scope("prototype")
    public LanguageService languageService(LanguageRepository languageRepository) {
        LanguageService languageService = new LanguageService();
        languageService.setLanguageRepository(languageRepository);

        return languageService;
    }

    @Bean
    public WordService wordService(WordRepository wordRepository) {
        return new WordService(wordRepository);
    }

    @Bean
    public TranslationRepository translationRepository() {
        return new TranslationRepository();
    }

    @Bean
    public WordRepository wordRepository() {
        return new WordRepository();
    }

    @Bean
    public LanguageRepository languageRepository() {
        return new LanguageRepository();
    }

    @Bean
    public DictionaryRepository dictionaryRepository() {
        return new DictionaryRepository();
    }
}
