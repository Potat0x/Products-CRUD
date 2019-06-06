package com.example.demo.domain;

import java.util.Objects;

final class Image {
  private final String url;

  public Image(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Image image = (Image) o;
    return Objects.equals(url, image.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url);
  }
}
