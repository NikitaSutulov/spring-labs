package com.brigade22.spring.springlabs.controllers;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.services.LanguageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/languages")
    public List<Language> listLanguages() {
        return languageService.getAll();
    }

    @GetMapping("/languages/create-language")
    public String createLanguageForm(Model model) {
        model.addAttribute("language", new Language(null, null));
        return "create-language";
    }

    @PostMapping("/languages/create-language")
    public String createLanguage(@ModelAttribute Language language) {
        if (language.getCode() == null || language.getName() == null) {
            return "redirect:/languages?user=admin";
        }

        languageService.saveLanguage(language);

        return "redirect:/languages?user=admin";
    }

    @GetMapping("/languages/edit-language/{code}")
    public String editLanguageForm(@PathVariable String code, Model model) {
        Language language = languageService.findByCode(code);

        model.addAttribute("language", language);
        return "edit-language";
    }

    @PostMapping("/languages/edit-language/{code}")
    public String editLanguageSubmit(@PathVariable String code, @ModelAttribute Language language) {
        languageService.updateLanguage(code, language);

        return "redirect:/languages?user=admin";
    }

    @DeleteMapping("/languages/{code}")
    public String deleteLanguage(@PathVariable String code) {
        languageService.deleteByCode(code);

        return "redirect:/languages?user=admin";
    }
}

