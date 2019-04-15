package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto {
    private final String name;
    private final PriceDto price;
    private final ImageDto image;
    private final DescriptionDto description;
    private final Set<TagDto> tags;

    @JsonCreator
    public ProductRequestDto(@JsonProperty("name") String name, @JsonProperty("price") PriceDto price, @JsonProperty("image") ImageDto image, @JsonProperty("description") DescriptionDto description, @JsonProperty("tags") Set<TagDto> tags) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.tags = tags;
    }

    @JsonGetter("name")
    String getName() {
        return name;
    }

    @JsonGetter("price")
    public PriceDto getPrice() {
        return price;
    }

    @JsonGetter("image")
    public ImageDto getImage() {
        return image;
    }

    @JsonGetter("description")
    public DescriptionDto getDescription() {
        return description;
    }

    @JsonGetter("tags")
    public Set<TagDto> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", image=" + image +
                ", description=" + description +
                ", tags=" + tags +
                '}';
    }

    boolean isValid() {
        return name != null && !name.isBlank();
    }
}
