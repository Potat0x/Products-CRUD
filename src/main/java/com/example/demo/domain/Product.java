package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

final public class Product {
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
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(createdAt, product.createdAt) &&
                Objects.equals(price, product.price) &&
                Objects.equals(image, product.image) &&
                Objects.equals(description, product.description) &&
                Objects.equals(tags, product.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdAt, price, image, description, tags);
    }
}
