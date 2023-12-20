package com.brigade22.spring.springlabs.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @Setter
    @NotBlank
    private String name;

    @Setter
    @Valid
    @ManyToOne
    private Language language1;

    @Setter
    @Valid
    @ManyToOne
    private Language language2;

    @OneToMany(mappedBy = "dictionary")
    private final List<Translation> translations = new ArrayList<>();

    public Dictionary() {
    }

    public Dictionary(String name, Language language1, Language language2) {
        this.name = name;
        this.language1 = language1;
        this.language2 = language2;
    }

    public Dictionary(long id, String name, Language language1, Language language2) {
        this.id = id;
        this.name = name;
        this.language1 = language1;
        this.language2 = language2;
    }

    public Translation addTranslation(Translation translation) {
        this.translations.add(translation);
        return translation;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations.clear();
        this.translations.addAll(translations);
    }
}
