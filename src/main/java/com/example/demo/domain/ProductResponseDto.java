package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductResponseDto {
    final String id;
    final String name;
    final PriceDto price;
    final ImageDto image;

    @JsonCreator
    public ProductResponseDto(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("price") PriceDto price, @JsonProperty("image") ImageDto image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
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

    @Override
    public String toString() {
        return "ProductResponseDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image=" + image +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductResponseDto)) return false;

        ProductResponseDto that = (ProductResponseDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        return image != null ? image.equals(that.image) : that.image == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }
}
