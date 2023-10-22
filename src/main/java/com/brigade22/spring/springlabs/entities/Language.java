package com.brigade22.spring.springlabs.entities;

import java.util.ArrayList;
import java.util.List;

public class Language {
    private String code;
    private String name;
    private final List<Word> words = new ArrayList<>();;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
