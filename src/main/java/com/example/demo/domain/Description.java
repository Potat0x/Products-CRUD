package com.example.demo.domain;

import java.util.Objects;

final class Description {
  private final String text;

  public Description(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  @Override
  public String toString() {
    return "Description{" + "text='" + text + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Description that = (Description) o;
    return Objects.equals(text, that.text);
  }

  @Override
  public int hashCode() {
    return text != null ? text.hashCode() : 0;
  }
}
