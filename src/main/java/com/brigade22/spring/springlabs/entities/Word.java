package com.brigade22.spring.springlabs.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @NotBlank
    private String value;

    @Getter
    @Setter
    @ManyToOne
    private Language language;

    @OneToMany(mappedBy = "word")
    private final List<Translation> translations = new ArrayList<>();

    public Word() {
    }

    public Word(Language language, String value) {
        this.language = language;
        this.value = value;
    }
}
