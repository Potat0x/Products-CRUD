package com.example.demo.domain;

import com.example.demo.domain.exceptions.InvalidDtoException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import java.util.Objects;

public class TagDto {
    private final String name;

    @JsonCreator
    public TagDto(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    static void assertValid(TagDto tag) {
        if (tag == null) {
            throw new InvalidDtoException("tags are required");
        }

        if (Strings.isNullOrEmpty(tag.getName())) {
            throw new InvalidDtoException("tag.name cannot be empty");
        }
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return Objects.equals(name, tagDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
