package com.brigade22.spring.springlabs.config;

import com.brigade22.spring.springlabs.repositories.DictionaryRepository;
import com.brigade22.spring.springlabs.repositories.LanguageRepository;
import com.brigade22.spring.springlabs.services.DictionaryService;
import com.brigade22.spring.springlabs.services.LanguageService;
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
    public LanguageRepository languageRepository() {
        return new LanguageRepository();
    }

    @Bean
    public DictionaryRepository dictionaryRepository() {
        return new DictionaryRepository();
    }
}
