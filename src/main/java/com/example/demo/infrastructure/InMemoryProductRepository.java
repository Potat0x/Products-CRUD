package com.example.demo.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Product;
import com.example.demo.domain.Tag;

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
    db.put(id, updatedProduct);
    return db.get(id);
  }

  @Override
  public void delete(String id) {
    db.remove(id);
  }

  @Override
  public List<Product> getAll() {
    return new ArrayList<>(db.values());
  }

  @Override
  public List<Product> getByTags(List<String> tags) {
    List<Product> productsByTags = new ArrayList<>();
    db.values()
        .forEach(
            product -> {
              if (product.getTags().stream()
                  .map(Tag::getName)
                  .collect(Collectors.toList())
                  .containsAll(tags)) {
                productsByTags.add(product);
              }
            });
    return productsByTags;
  }
}
