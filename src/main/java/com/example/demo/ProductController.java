package com.example.demo;

import com.example.demo.domain.ProductFacade;
import com.example.demo.domain.ProductRequestDto;
import com.example.demo.domain.ProductResponseDto;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{id}")
    ProductResponseDto updateProduct(@PathVariable(value = "id") String id, @RequestParam("name") String name) {
        return productFacade.update(id, name);
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable("id") String id) {
        productFacade.delete(id);
    }
}
