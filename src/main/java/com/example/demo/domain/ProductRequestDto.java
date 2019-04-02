package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto {
    private final String name;

    @JsonCreator
    public ProductRequestDto(@JsonProperty("name") String name) {
        this.name = name;
    }

    @JsonGetter("name")
    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "name='" + name + '\'' +
                '}';
    }

    boolean isValid() {
        return name != null && !name.isBlank();
    }
}
