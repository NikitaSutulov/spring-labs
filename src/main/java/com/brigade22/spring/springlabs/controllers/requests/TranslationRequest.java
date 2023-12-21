package com.brigade22.spring.springlabs.controllers.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationRequest {
    @NotBlank
    private String word;
    @NotBlank
    private String translatedWord;

}
