package com.brigade22.spring.springlabs.controllers;

import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.services.DictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/dictionaries")
    public String listLanguages(Model model, @RequestParam(name = "user", required = false) String user) {
        List<Dictionary> dictionaries = dictionaryService.getAll();

        model.addAttribute("dictionaries", dictionaries);
        model.addAttribute("user", user);
        return "dictionaries";
    }

    @GetMapping("/dictionaries/{name}")
    public String openDictionary(@PathVariable String name, Model model, @RequestParam(name = "user", required = false) String user) {

        Dictionary dictionary = dictionaryService.getDictionaryByName(name);

        if (dictionary != null) {
            model.addAttribute("dictionary", dictionary);
            model.addAttribute("user", user);
            return "dictionary";
        } else {
            return "error";
        }
    }

    @GetMapping("dictionaries/{dictionaryName}/edit")
    public String editDictionary(@PathVariable String dictionaryName, Model model) {
        Dictionary dictionary = dictionaryService.getDictionaryByName(dictionaryName);
        model.addAttribute("dictionary", dictionary);
        return "edit-dictionary";
    }

    @PostMapping("dictionaries/{dictionaryName}/edit")
    public String editDictionary(@PathVariable String dictionaryName, @RequestParam("name") String updatedName) {
        Dictionary dictionary = dictionaryService.getDictionaryByName(dictionaryName);
        dictionary.setName(updatedName);
        return "redirect:/dictionaries?user=admin";
    }

    @GetMapping("/dictionaries/{dictionaryName}/edit/{word}-{translatedWord}")
    public String editTranslation(
        @PathVariable String dictionaryName,
        @PathVariable String word,
        @PathVariable String translatedWord,
        Model model
    ) {
        Translation translation = dictionaryService.checkTranslation(dictionaryName, word, translatedWord);

        if (translation != null) {
            model.addAttribute("translation", translation);
        }
        return "edit-translation";
    }

    @PutMapping("/dictionaries/{dictionaryName}/{word}-{translatedWord}")
    public String editTranslation(
        @PathVariable String dictionaryName,
        @PathVariable String word,
        @PathVariable String translatedWord,
        @RequestParam("word") String updatedWord,
        @RequestParam("translatedWord") String updatedTranslatedWord
    ) {
        dictionaryService.updateTranslation(dictionaryName, word, translatedWord, updatedWord, updatedTranslatedWord);

        return "redirect:/dictionaries/" + dictionaryName + "?user=admin";
    }

    @GetMapping("/dictionaries/create-dictionary")
    public String showCreateDictionaryForm(Model model) {
        model.addAttribute("dictionary", new Dictionary(null, null, null));
        return "create-dictionary";
    }

    @PostMapping("/dictionaries/create-dictionary")
    public String createDictionary(@ModelAttribute Dictionary dictionary) {
        if (dictionary.getName() != null) {
            dictionaryService.saveDictionary(dictionary);
        }

        return "redirect:/dictionaries";
    }

    @DeleteMapping("/dictionaries/{name}")
    public String deleteLanguage(@PathVariable String name) {
        dictionaryService.deleteDictionary(name);

        return "redirect:/dictionaries?user=admin";
    }

    @GetMapping("/dictionaries/{dictionaryName}/create-translation")
    public String showCreateTranslationForm(@PathVariable String dictionaryName, Model model) {
        model.addAttribute("dictionaryName", dictionaryName);
        model.addAttribute("translation", new Translation(dictionaryService.getDictionaryByName(dictionaryName), null, null));
        return "create-translation";
    }

    @PostMapping("/dictionaries/{dictionaryName}/create-translation")
    public String createTranslation(
        @PathVariable String dictionaryName,
        @RequestParam("word") String word,
        @RequestParam("translatedWord") String translatedWord
    ) {
        if (word != null && translatedWord != null) {
            dictionaryService.addTranslation(dictionaryName, word, translatedWord);
        }

        return "redirect:/dictionaries/" + dictionaryName + "?user=admin";
    }

    @DeleteMapping("/dictionaries/{dictionaryName}/delete-translation/{word}-{translatedWord}")
    public String deleteTranslation(
        @PathVariable String dictionaryName,
        @PathVariable String word,
        @PathVariable String translatedWord
    ) {
        dictionaryService.deleteTranslation(dictionaryName, word, translatedWord);

        return "redirect:/dictionaries/" + dictionaryName + "?user=admin";
    }

    @GetMapping("/dictionaries/{dictionaryName}/search")
    public String searchTranslation(
        @PathVariable String dictionaryName,
        @RequestParam("word") String word,
        Model model
    ) {
        if (word == null) {
            return "";
        }

        Word result = dictionaryService.getTranslationForWord(dictionaryName, word);

        model.addAttribute("word", word);
        model.addAttribute("result", result);
        return "translation";

    }
}
