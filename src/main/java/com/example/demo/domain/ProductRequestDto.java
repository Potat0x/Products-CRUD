package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto {
    private final String name;
    private final PriceDto price;
    private final ImageDto image;
    private final DescriptionDto description;

    @JsonCreator
    public ProductRequestDto(@JsonProperty("name") String name, @JsonProperty("price") PriceDto price, @JsonProperty("image") ImageDto image, @JsonProperty("description") DescriptionDto description) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
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

    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", image=" + image +
                ", description=" + description +
                '}';
    }

    boolean isValid() {
        return name != null && !name.isBlank();
    }
}
