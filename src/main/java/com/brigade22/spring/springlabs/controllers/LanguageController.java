package com.brigade22.spring.springlabs.controllers;

import com.brigade22.spring.springlabs.entities.Language;
import com.brigade22.spring.springlabs.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LanguageController {

    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/languages")
    public String listLanguages(Model model, @RequestParam(name = "user", required = false) String user) {
        List<Language> languages = languageService.getAll();
        model.addAttribute("languages", languages);
        model.addAttribute("user", user); // Pass the user role to the template
        return "languages"; // This maps to the Thymeleaf template
    }

    // Add controller methods for other language-related actions
    @GetMapping("/languages/create-language") // Adjust the mapping
    public String createLanguageForm(Model model) {
        model.addAttribute("language", new Language(null, null)); // Pass an empty language object to the template
        return "create-language";
    }

    @PostMapping("/languages/create-language") // Adjust the mapping
    public String createLanguage(@ModelAttribute Language language) {
        if (language.getCode() == null || language.getName() == null) {
            return "redirect:/languages?user=admin"; // Redirect to the main languages page
        }

        language.setCode(language.getCode().toLowerCase());

        if (languageService.findByCode(language.getCode()) != null) {
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
        // Retrieve the existing language
        Language existingLanguage = languageService.findByCode(code);
        language.setCode(language.getCode().toLowerCase());

        if (existingLanguage != null) {
            // Update the language properties from the form data
            if (languageService.findByCode(language.getCode()) == null) {
                existingLanguage.setCode(language.getCode());
            }
            existingLanguage.setName(language.getName());

        }

        return "redirect:/languages?user=admin";
    }

    @DeleteMapping("/languages/{code}")
    public String deleteLanguage(@PathVariable String code) {
        languageService.deleteByCode(code);
        return "redirect:/languages?user=admin";
    }
}

