package com.brigade22.spring.springlabs.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = "Language.findByCode", query = "SELECT l FROM Language l WHERE l.code = :code")
public class Language {
    @Setter
    @Getter
    @NotBlank
    @Id
    private String code;
    @Setter
    @Getter
    @NotBlank
    private String name;
    @OneToMany(mappedBy = "language")
    private final List<Word> words = new ArrayList<>();

    public Language() {
    }

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void addWord(Word word) {
        this.words.add(word);
    }
}
