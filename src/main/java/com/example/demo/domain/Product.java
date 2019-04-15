package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.Set;

public final class Product {
    private final String id;
    private final String name;
    private final LocalDateTime createdAt;
    private final Price price;
    private final Image image;
    private final Description description;
    private final Set<Tag> tags;

    public Product(String id, String name, LocalDateTime createdAt, Price price, Image image, Description description, Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.image = image;
        this.description = description;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Price getPrice() {
        return price;
    }

    public Image getImage() {
        return image;
    }

    public Description getDescription() {
        return description;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Product withNewName(String newName) {
        return new Product(id, newName, createdAt, price, image, description, tags);
    }

    public Product withNewImage(Image newImage) {
        return new Product(id, name, createdAt, price, newImage, description, tags);
    }

    public Product withNewPrice(Price newPrice) {
        return new Product(id, name, createdAt, newPrice, image, description, tags);
    }

    public Product withNewDescription(Description newDescription) {
        return new Product(id, name, createdAt, price, image, newDescription, tags);
    }

    public Product withNewTags(Set<Tag> newTags) {
        return new Product(id, name, createdAt, price, image, description, newTags);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", image=" + image +
                ", description=" + description +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (createdAt != null ? !createdAt.equals(product.createdAt) : product.createdAt != null) return false;
        if (price != null ? !price.equals(product.price) : product.price != null) return false;
        if (image != null ? !image.equals(product.image) : product.image != null) return false;
        if (description != null ? !description.equals(product.description) : product.description != null) return false;
        return tags != null ? tags.equals(product.tags) : product.tags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

}
