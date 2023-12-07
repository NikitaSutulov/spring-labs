package com.brigade22.spring.springlabs.controllers;

import com.brigade22.spring.springlabs.controllers.requests.TranslationRequest;
import com.brigade22.spring.springlabs.controllers.responses.DictionaryResponse;
import com.brigade22.spring.springlabs.controllers.responses.GetDictionariesResponse;
import com.brigade22.spring.springlabs.controllers.responses.TranslationResponse;
import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.services.DictionaryService;
import com.brigade22.spring.springlabs.services.LanguageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dictionaries")
@Validated
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final LanguageService languageService;

    public DictionaryController(DictionaryService dictionaryService, LanguageService languageService) {
        this.dictionaryService = dictionaryService;
        this.languageService = languageService;
    }

    @GetMapping
    @Valid
    public ResponseEntity<GetDictionariesResponse> getDictionaries(
            @RequestParam(name = "code", required = false) @NotBlank String code,
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {

        List<Dictionary> filteredDictionaries = dictionaryService.getAll();

        if (code != null && !code.isBlank()) {
            filteredDictionaries = filteredDictionaries.stream()
                    .filter((dictionary -> dictionary.getLanguage1().getCode().equals(code) ||
                            dictionary.getLanguage2().getCode().equals(code)))
                    .collect(Collectors.toList());
        }

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, filteredDictionaries.size());

        List<Dictionary> paginatedDictionaries = filteredDictionaries.subList(startIndex, endIndex);

        return ResponseEntity.ok(new GetDictionariesResponse(paginatedDictionaries));
    }

    @GetMapping("{id}")
    public ResponseEntity<DictionaryResponse> openDictionary(@PathVariable Long id) {
        Dictionary dictionary = dictionaryService.getDictionaryById(id);
        if (dictionary == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Dictionary not found"
            );
        }

        return ResponseEntity.ok(new DictionaryResponse(dictionary));
    }

    @PutMapping("{id}")
    public ResponseEntity<DictionaryResponse> editDictionary(@PathVariable Long id, @Valid @RequestBody Dictionary requestDictionary) {
        Dictionary dictionary = dictionaryService.getDictionaryById(id);
        if (dictionary == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Dictionary not found"
            );
        }

        try {
            Dictionary changedDictionary = dictionaryService.updateDictionary(id, requestDictionary.getName(), requestDictionary.getLanguage1(), requestDictionary.getLanguage2());
            return ResponseEntity.ok(new DictionaryResponse(changedDictionary));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Data is wrong"
            );
        }
    }

    @PutMapping("{id}/{word}-{translatedWord}")
    public ResponseEntity<TranslationResponse> editTranslation(
            @PathVariable Long id,
            @PathVariable String word,
            @PathVariable String translatedWord,
            @Valid @RequestBody TranslationRequest translation
    ) {
        try {
            dictionaryService.updateTranslation(id, word, translatedWord, translation.getWord(), translation.getTranslatedWord());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Translation not found"
            );
        }

        return ResponseEntity.ok(new TranslationResponse(translation.getWord(), translation.getTranslatedWord()));
    }

    @PostMapping
    public ResponseEntity<DictionaryResponse> createDictionary(@Valid @RequestBody Dictionary requestDictionary) {
        if (languageService.findByCode(requestDictionary.getLanguage1().getCode()) == null) {
            languageService.saveLanguage(requestDictionary.getLanguage1());
        }
        Language language1 = languageService.findByCode(requestDictionary.getLanguage1().getCode());
        if (languageService.findByCode(requestDictionary.getLanguage2().getCode()) == null) {
            languageService.saveLanguage(requestDictionary.getLanguage2());
        }
        Language language2 = languageService.findByCode(requestDictionary.getLanguage2().getCode());

        if (!Objects.equals(requestDictionary.getLanguage1().getName(), language1.getName()) || !Objects.equals(requestDictionary.getLanguage2().getName(), language2.getName())) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Language name is wrong"
            );
        }

        Dictionary dictionary = new Dictionary(
                requestDictionary.getName(),
                language1,
                language2
        );

        dictionary = dictionaryService.saveDictionary(dictionary);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dictionary.getId())
                .toUri();

        return ResponseEntity.created(location).body(new DictionaryResponse(dictionary));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<DictionaryResponse> deleteLanguage(@PathVariable Long id) {
        Dictionary dictionary = dictionaryService.deleteDictionaryById(id);

        if (dictionary == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Dictionary not found"
            );
        }

        return ResponseEntity.ok(new DictionaryResponse(dictionary));
    }

    @PostMapping("{id}")
    public ResponseEntity<TranslationResponse> createTranslation(
            @PathVariable Long id,
            @Valid @RequestBody TranslationRequest translationRequest
    ) {
        if (dictionaryService.getDictionaryById(id) == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Dictionary not found"
            );
        }

        dictionaryService.addTranslation(id, translationRequest.getWord(), translationRequest.getTranslatedWord());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}/{word}-{translateWord}")
                .buildAndExpand(id, translationRequest.getWord(), translationRequest.getTranslatedWord())
                .toUri();

        return ResponseEntity.created(location).body(new TranslationResponse(translationRequest.getWord(), translationRequest.getTranslatedWord()));
    }

    @GetMapping("{id}/{word}")
    public ResponseEntity<TranslationResponse> searchTranslation(
            @PathVariable Long id,
            @PathVariable String word
    ) {
        Word result = dictionaryService.getTranslationForWord(id, word);

        if (result == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Translation not found"
            );
        }

        return ResponseEntity.ok(new TranslationResponse(word, result.getValue()));
    }
}
