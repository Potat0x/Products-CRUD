package com.example.demo.infrastructure;

import com.example.demo.domain.Product;
import org.springframework.stereotype.Repository;

public interface ProductRepository {
    void save(Product product);//normalnie: ProductEntity między pakietami
}
