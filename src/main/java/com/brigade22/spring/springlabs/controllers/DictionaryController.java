package com.brigade22.spring.springlabs.controllers;

import com.brigade22.spring.springlabs.controllers.requests.DictionaryRequest;
import com.brigade22.spring.springlabs.controllers.requests.TranslationRequest;
import com.brigade22.spring.springlabs.controllers.responses.DictionaryResponse;
import com.brigade22.spring.springlabs.controllers.responses.GetDictionariesResponse;
import com.brigade22.spring.springlabs.controllers.responses.TranslationResponse;
import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.services.DictionaryService;
import com.brigade22.spring.springlabs.services.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Dictionaries", description = "Operations related to dictionaries")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final LanguageService languageService;

    public DictionaryController(DictionaryService dictionaryService, LanguageService languageService) {
        this.dictionaryService = dictionaryService;
        this.languageService = languageService;
    }

    @GetMapping
    @Valid
    @Operation(
            summary = "Get Dictionaries",
            description = "Get a list of dictionaries with optional filtering by language code.",
            parameters = {
                    @Parameter(name = "code", description = "Language code to filter dictionaries."),
                    @Parameter(name = "page", description = "Page number (default: 0)."),
                    @Parameter(name = "size", description = "Number of items per page (default: 10).")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved dictionaries.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetDictionariesResponse.class))),
    })
    public ResponseEntity<GetDictionariesResponse> getDictionaries(
            @RequestParam(name = "code", required = false) String code,
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
    @Operation(
            summary = "Open Dictionary",
            description = "Get details of a specific dictionary by ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the dictionary.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DictionaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dictionary not found.",
                    content = @Content)
    })
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
    @Operation(
            summary = "Edit Dictionary",
            description = "Edit the details of a specific dictionary by ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edited the dictionary.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DictionaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or data is wrong.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Dictionary not found.",
                    content = @Content)
    })
    public ResponseEntity<DictionaryResponse> editDictionary(@PathVariable Long id, @Valid @RequestBody DictionaryRequest requestDictionary) {
        languageService.checkIfLanguageExists(requestDictionary);

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
    @Operation(
            summary = "Edit Translation",
            description = "Edit the translation of a word in a dictionary."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edited the translation.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TranslationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Translation not found.",
                    content = @Content)
    })
    public ResponseEntity<TranslationResponse> editTranslation(
            @PathVariable Long id,
            @PathVariable String word,
            @PathVariable String translatedWord,
            @Valid @RequestBody TranslationRequest translation
    ) {
        if (dictionaryService.getDictionaryById(id) == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Dictionary not found"
            );
        }

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
    @Operation(
            summary = "Create Dictionary",
            description = "Create a new dictionary with the specified details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the dictionary.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DictionaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or language name is wrong.",
                    content = @Content)
    })
    public ResponseEntity<DictionaryResponse> createDictionary(@Valid @RequestBody DictionaryRequest requestDictionary) {
        languageService.checkIfLanguageExists(requestDictionary);

        Language language1 = languageService.findByCode(requestDictionary.getLanguage1().getCode());
        Language language2 = languageService.findByCode(requestDictionary.getLanguage2().getCode());

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
    @Operation(
            summary = "Delete Dictionary",
            description = "Delete a dictionary by ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the dictionary.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DictionaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dictionary not found.",
                    content = @Content)
    })
    public ResponseEntity<DictionaryResponse> deleteDictionary(@PathVariable Long id) {
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
    @Operation(
            summary = "Create Translation",
            description = "Create a new translation for a word in a dictionary."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the translation.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TranslationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or dictionary not found.",
                    content = @Content)
    })
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

        String word = translationRequest.getWord();
        String translatedWord = translationRequest.getTranslatedWord();

        dictionaryService.addTranslation(id, word, translatedWord);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/" + word + "-" + translatedWord)
                .buildAndExpand(id, translationRequest.getWord(), translationRequest.getTranslatedWord())
                .toUri();

        return ResponseEntity.created(location).body(new TranslationResponse(translationRequest.getWord(), translationRequest.getTranslatedWord()));
    }

    @GetMapping("{id}/{word}")
    @Operation(
            summary = "Search Translation",
            description = "Search for the translation of a word in a dictionary."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the translation.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TranslationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Translation not found.",
                    content = @Content)
    })
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
