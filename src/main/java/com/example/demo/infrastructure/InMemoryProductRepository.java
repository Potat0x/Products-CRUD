package com.example.demo.infrastructure;

import com.example.demo.domain.Product;
import com.example.demo.domain.Tag;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> db = new HashMap<>();

    @Override
    public void save(Product product) {
        db.put(product.getId(), product);
    }

    @Override
    public Product find(String id) {
        return db.get(id);
    }

    @Override
    public Product update(String id, Product updatedProduct) {
        Product productToUpdate = db.get(id);
        db.put(productToUpdate.getId(), updatedProduct);
        return db.get(productToUpdate.getId());
    }

    @Override
    public void delete(String id) {
        db.remove(id);
    }

    @Override
    public List<Product> getAll() {
        return db.keySet().stream().map(db::get).collect(Collectors.toList());
    }

    @Override
    public List<Product> getByTags(List<String> tags) {
        List<Product> productsByTags = new ArrayList<>();
        db.keySet().forEach(id -> {
            Product product = db.get(id);
            if (product.getTags().stream().map(Tag::getName).collect(Collectors.toList()).containsAll(tags)) {
                productsByTags.add(product);
            }
        });
        return productsByTags;
    }
}
