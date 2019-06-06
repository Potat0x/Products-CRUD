package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import java.util.Objects;

import com.example.demo.domain.exceptions.InvalidDtoException;

public final class DescriptionDto {
  private final String text;

  @JsonCreator
  public DescriptionDto(@JsonProperty("text") String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  static void assertValid(DescriptionDto description) {
    if (description == null) {
      throw new InvalidDtoException("description is required");
    }

    if (Strings.isNullOrEmpty(description.getText())) {
      throw new InvalidDtoException("description.text cannot be empty");
    }
  }

  @Override
  public String toString() {
    return "DescriptionDto{" + "text='" + text + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DescriptionDto that = (DescriptionDto) o;
    return Objects.equals(text, that.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }
}
