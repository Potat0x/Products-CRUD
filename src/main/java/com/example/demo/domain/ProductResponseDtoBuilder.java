package com.example.demo.domain;

import java.util.Set;

final class ProductResponseDtoBuilder {
  private String id;
  private String name;
  private PriceDto price;
  private ImageDto image;
  private DescriptionDto description;
  private Set<TagDto> tags;

  public ProductResponseDto build() {
    return new ProductResponseDto(id, name, price, image, description, tags);
  }

  public ProductResponseDtoBuilder setId(String id) {
    this.id = id;
    return this;
  }

  public ProductResponseDtoBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public ProductResponseDtoBuilder setPrice(PriceDto price) {
    this.price = price;
    return this;
  }

  public ProductResponseDtoBuilder setImage(ImageDto image) {
    this.image = image;
    return this;
  }

  public ProductResponseDtoBuilder setDescription(DescriptionDto description) {
    this.description = description;
    return this;
  }

  public ProductResponseDtoBuilder setTags(Set<TagDto> tags) {
    this.tags = tags;
    return this;
  }
}
