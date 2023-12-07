package com.brigade22.spring.springlabs.controllers;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.services.LanguageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public ResponseEntity<List<Language>> getLanguages() {
        return ResponseEntity.ok(languageService.getAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Language> findLanguage(@PathVariable String code) {
        Language language = languageService.findByCode(code);

        if (language == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Language is not found"
            );
        }

        return ResponseEntity.ok(language);
    }

    @PostMapping
    public ResponseEntity<Language> createLanguage(@Valid @RequestBody Language languageDto) {
        if (languageService.findByCode(languageDto.getCode()) != null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Language with such a code already exists"
            );
        }

        if (languageService.findByName(languageDto.getName()) != null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Language with such a name already exists"
            );
        }

        Language language = new Language(languageDto.getCode(), languageDto.getName());
        language = languageService.saveLanguage(language);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(language.getCode())
                .toUri();

        return ResponseEntity.created(location).body(language);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Language> editLanguage(
            @PathVariable String code,
            @Valid @RequestBody Language languageDto) {

        if (languageService.findByName(languageDto.getName()) != null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Language with such a name already exists"
            );
        }

        Language language = languageService.updateLanguage(code, languageDto);

        return ResponseEntity.ok(language);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Language> deleteLanguage(@PathVariable String code) {
        Language language = languageService.deleteByCode(code);
        if (language == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Language is not found"
            );
        }

        return ResponseEntity.ok(language);
    }
}
