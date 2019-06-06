package com.example.demo.domain;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Set;

import com.example.demo.domain.exceptions.ProductBuilderException;

final class ProductBuilder {
  private String id;
  private String name;
  private LocalDateTime createdAt;
  private Price price;
  private Image image;
  private Description description;
  private Set<Tag> tags;

  public Product build() {
    assertAllFieldsInitialized();
    return new Product(id, name, createdAt, price, image, description, tags);
  }

  public ProductBuilder setId(String id) {
    this.id = id;
    return this;
  }

  public ProductBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public ProductBuilder setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public ProductBuilder setPrice(Price price) {
    this.price = price;
    return this;
  }

  public ProductBuilder setImage(Image image) {
    this.image = image;
    return this;
  }

  public ProductBuilder setDescription(Description description) {
    this.description = description;
    return this;
  }

  public ProductBuilder setTags(Set<Tag> tags) {
    this.tags = tags;
    return this;
  }

  private void assertAllFieldsInitialized() {
    for (Field field : getClass().getDeclaredFields()) {
      try {
        Object member = field.get(this);
        System.out.println(member);
        if (member == null) {
          throw new ProductBuilderException(
              "Uninitialized field: " + field.getType().getSimpleName() + " " + field.getName());
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        throw new ProductBuilderException("field is inaccessible");
      }
    }
  }
}
