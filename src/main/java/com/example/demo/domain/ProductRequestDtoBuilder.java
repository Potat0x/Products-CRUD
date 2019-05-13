package com.example.demo.domain;

import java.util.Set;

public final class ProductRequestDtoBuilder {
    private String name;
    private PriceDto price;
    private ImageDto image;
    private DescriptionDto description;
    private Set<TagDto> tags;

    public ProductRequestDto build() {
        return new ProductRequestDto(name, price, image, description, tags);
    }

    public ProductRequestDtoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductRequestDtoBuilder setPrice(PriceDto price) {
        this.price = price;
        return this;
    }

    public ProductRequestDtoBuilder setImage(ImageDto image) {
        this.image = image;
        return this;
    }

    public ProductRequestDtoBuilder setDescription(DescriptionDto description) {
        this.description = description;
        return this;
    }

    public ProductRequestDtoBuilder setTags(Set<TagDto> tags) {
        this.tags = tags;
        return this;
    }
}