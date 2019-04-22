package com.example.demo;

import com.example.demo.domain.ProductFacade;
import com.example.demo.domain.ProductListResponseDto;
import com.example.demo.domain.ProductRequestDto;
import com.example.demo.domain.ProductResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
class ProductController {

    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @PostMapping()
    ProductResponseDto createProduct(@RequestBody ProductRequestDto rd) {
        return productFacade.create(rd);
    }

    @GetMapping("/{id}")
    ProductResponseDto getProduct(@PathVariable("id") String id) {
        return productFacade.find(id);
    }

    @GetMapping
    ProductListResponseDto getProducts(@RequestParam(required = false, name = "tag") List<String> tags) {
        return productFacade.getProducts(tags);
    }

    @PutMapping("/{id}")
    ProductResponseDto updateProduct(@PathVariable("id") String id, @RequestBody ProductRequestDto rd) {
        return productFacade.update(id, rd);
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable("id") String id) {
        productFacade.delete(id);
    }
}
