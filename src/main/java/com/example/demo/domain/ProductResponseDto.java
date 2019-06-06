package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class ProductResponseDto {
  private final String id;
  private final String name;
  private final PriceDto price;
  private final ImageDto image;
  private final DescriptionDto description;
  private final Set<TagDto> tags;

  @JsonCreator
  public ProductResponseDto(
      @JsonProperty("id") String id,
      @JsonProperty("name") String name,
      @JsonProperty("price") PriceDto price,
      @JsonProperty("image") ImageDto image,
      @JsonProperty("description") DescriptionDto description,
      @JsonProperty("tags") Set<TagDto> tags) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.image = image;
    this.description = description;
    this.tags = nullToEmpty(tags);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public PriceDto getPrice() {
    return price;
  }

  public ImageDto getImage() {
    return image;
  }

  public DescriptionDto getDescription() {
    return description;
  }

  public Set<TagDto> getTags() {
    return nullToEmpty(tags);
  }

  private Set<TagDto> nullToEmpty(Set<TagDto> set) {
    return set != null ? set : Collections.emptySet();
  }

  @Override
  public String toString() {
    return "ProductResponseDto{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", price="
        + price
        + ", image="
        + image
        + ", description="
        + description
        + ", tags="
        + tags
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductResponseDto that = (ProductResponseDto) o;
    return Objects.equals(id, that.id)
        && Objects.equals(name, that.name)
        && Objects.equals(price, that.price)
        && Objects.equals(image, that.image)
        && Objects.equals(description, that.description)
        && Objects.equals(tags, that.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, image, description, tags);
  }
}
