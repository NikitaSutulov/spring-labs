package com.brigade22.spring.springlabs.controllers.requests;

import com.brigade22.spring.springlabs.entities.Language;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DictionaryRequest {

    @NotBlank
    private String name;

    @Valid
    private Language language1;

    @Valid
    private Language language2;

}
