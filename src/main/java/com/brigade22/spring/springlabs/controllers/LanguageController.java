package com.brigade22.spring.springlabs.controllers;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.services.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/languages")
@Tag(name = "Languages", description = "Operations related to languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    @Operation(
            summary = "Get Languages",
            description = "Get a list of all languages."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved languages.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Language.class))
    )
    public ResponseEntity<List<Language>> getLanguages() {
        return ResponseEntity.ok(languageService.getAll());
    }

    @GetMapping("/{code}")
    @Operation(
            summary = "Find Language",
            description = "Find a language by its code."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the language.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Language.class))
            ),
            @ApiResponse(responseCode = "404", description = "Language not found.", content = @Content)
    })
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
    @Operation(
            summary = "Create Language",
            description = "Create a new language with the specified details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created the language.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Language.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request or language already exists.", content = @Content)
    })
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
    @Operation(
            summary = "Edit Language",
            description = "Edit the details of a specific language by code."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully edited the language.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Language.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request or language already exists.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Language not found.", content = @Content)
    })
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
    @Operation(
            summary = "Delete Language",
            description = "Delete a language by code."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted the language.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Language.class))
            ),
            @ApiResponse(responseCode = "404", description = "Language not found.", content = @Content)
    })
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
