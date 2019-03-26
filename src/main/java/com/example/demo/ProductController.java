package com.example.demo;

import com.example.demo.domain.ProductFacade;
import com.example.demo.domain.ProductRequestDto;
import com.example.demo.domain.ProductResponseDto;
import org.springframework.web.bind.annotation.*;

//@RestController("api/products")
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @PostMapping("/add")
    ProductResponseDto createProduct(@RequestBody ProductRequestDto rd) {
        return productFacade.create(rd);
    }

    @GetMapping("/{id}")
    ProductResponseDto getProduct(@PathVariable("id") String id) {
        return productFacade.find(id);
    }
}
