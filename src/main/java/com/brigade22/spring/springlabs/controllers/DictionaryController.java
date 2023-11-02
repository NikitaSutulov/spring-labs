package com.brigade22.spring.springlabs.controllers;

import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import com.brigade22.spring.springlabs.services.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/dictionaries")
    public String listLanguages(Model model, @RequestParam(name = "user", required = false) String user) {
        List<Dictionary> dictionaries = dictionaryService.getAll();
        model.addAttribute("dictionaries", dictionaries);
        model.addAttribute("user", user); // Pass the user role to the template
        return "dictionaries"; // This maps to the Thymeleaf template
    }

    @GetMapping("/dictionaries/{name}")
    public String openDictionary(@PathVariable String name, Model model, @RequestParam(name = "user", required = false) String user) {
        // Here, you should retrieve the dictionary by name from your data source
        // For the sake of example, we assume you have a "Dictionary" class with "name" and "translations" properties.

        Dictionary dictionary = dictionaryService.getDictionaryByName(name);

        if (dictionary != null) {
            model.addAttribute("dictionary", dictionary);
            model.addAttribute("user", user); // Pass the user role to the template
            return "dictionary"; // This maps to the Thymeleaf template "dictionary.html"
        } else {
            // Handle dictionary not found, redirect to an error page or display a message
            return "error"; // You can create an error Thymeleaf template
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
            Model model) {
        Dictionary dictionary = dictionaryService.getDictionaryByName(dictionaryName);
        for (Translation translation : dictionary.getTranslations()) {
            if (translation.getWord().getValue().equals(word) && translation.getTranslatedWord().getValue().equals(translatedWord)) {
                model.addAttribute("translation", translation);
                return "edit-translation";
            }
        }
        return "edit-translation";
    }

    @PostMapping("/dictionaries/{dictionaryName}/edit/{word}-{translatedWord}")
    public String editTranslation(
            @PathVariable String dictionaryName,
            @PathVariable String word,
            @PathVariable String translatedWord,
            @RequestParam("word") String updatedWord,
            @RequestParam("translatedWord") String updatedTranslatedWord) {
        Dictionary dictionary = dictionaryService.getDictionaryByName(dictionaryName);
        for (Translation translationSeek : dictionary.getTranslations()) {
            if (Objects.equals(translationSeek.getWord().getValue(), word) && Objects.equals(translationSeek.getTranslatedWord().getValue(), translatedWord)) {
                Language wordLanguage = translationSeek.getWord().getLanguage();
                Language translatedWordLanguage = translationSeek.getTranslatedWord().getLanguage();
                Word newWord = new Word(wordLanguage, updatedWord);
                Word newTranslatedWord = new Word(translatedWordLanguage, updatedTranslatedWord);
                translationSeek.setWord(newWord);
                translationSeek.setTranslatedWord(newTranslatedWord);
                break;
            }
        }
        return "redirect:/dictionaries/" + dictionaryName + "?user=admin";
    }

    @GetMapping("/dictionaries/create-dictionary")
    public String showCreateDictionaryForm(Model model) {
        model.addAttribute("dictionary", new Dictionary(null));
        return "create-dictionary"; // This should match the name of your Thymeleaf template file.
    }

    @PostMapping("/dictionaries/create-dictionary")
    public String createDictionary(@ModelAttribute Dictionary dictionary) {
        if (dictionary.getName() != null) {
            dictionaryService.saveDictionary(dictionary);
        }
        return "redirect:/dictionaries"; // Redirect to a different page after dictionary creation.
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
        return "create-translation"; // This should match the name of your Thymeleaf template file.
    }

    @PostMapping("/dictionaries/{dictionaryName}/create-translation")
    public String createTranslation(
            @PathVariable String dictionaryName,
            @RequestParam("word") String word,
            @RequestParam("translatedWord") String translatedWord) {
//        if (word != null && translatedWord != null) {
//            Dictionary dictionary = dictionaryService.getDictionaryByName(dictionaryName);
//
////            Translation translation = new Translation(dictionary, word, translatedWord);
//            dictionary.addTranslation(translation);
//            dictionaryService.saveDictionary(dictionary);
//        }
        return "redirect:/dictionaries/" + dictionaryName + "?user=admin"; // Redirect to a different page after dictionary creation.
    }
}
