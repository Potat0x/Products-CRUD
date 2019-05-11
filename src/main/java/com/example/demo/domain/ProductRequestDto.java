package com.example.demo.domain;

import com.example.demo.domain.exceptions.InvalidDtoException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto {
    private final String name;
    private final PriceDto price;
    private final ImageDto image;
    private final DescriptionDto description;
    private final Set<TagDto> tags;

    @JsonCreator
    public ProductRequestDto(@JsonProperty("name") String name,
                             @JsonProperty("price") PriceDto price,
                             @JsonProperty("image") ImageDto image,
                             @JsonProperty("description") DescriptionDto description,
                             @JsonProperty("tags") Set<TagDto> tags) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.tags = tags;
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
        return tags;
    }

    public void assertValid() {
        if (Strings.isNullOrEmpty(name)) {
            throw new InvalidDtoException("product name cannot be empty");
        }

        if (tags == null || tags.isEmpty()) {
            throw new InvalidDtoException("tags are required");
        }
        tags.forEach(TagDto::assertValid);

        PriceDto.assertValid(price);
        ImageDto.assertValid(image);
        DescriptionDto.assertValid(description);
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
}
