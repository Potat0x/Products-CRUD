package com.example.demo.domain;

import com.example.demo.infrastructure.ProductRepository;
import com.example.demo.infrastructure.exceptions.EmptyProductNameException;
import com.example.demo.infrastructure.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
class ProductFacadeImpl implements ProductFacade {

    private final ProductRepository repository;

    public ProductFacadeImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductResponseDto create(ProductRequestDto requestDto) {
        assertRequestValid(requestDto);

        String id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Product product = new Product(id, requestDto.getName(), now, priceDtoToPrice(requestDto.getPrice()), new Image(requestDto.getImage().getUrl()));

        repository.save(product);
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(), product.getName(), priceToDto(product.getPrice()), requestDto.getImage());
        return productResponseDto;
    }

    @Override
    public ProductResponseDto find(String id) {
        assertProductExists(id);
        Product product = repository.find(id);
        return new ProductResponseDto(product.getId(), product.getName(), priceToDto(product.getPrice()), new ImageDto(product.getImage().getUrl()));
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto requestDto) {
        assertProductExists(id);
        assertRequestValid(requestDto);

        Product product = repository.find(id);
        Product updatedProduct = repository.update(id, product.withNewName(requestDto.getName()).withNewImage(new Image(requestDto.getImage().getUrl())));
        return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName(), priceToDto(updatedProduct.getPrice()), requestDto.getImage());
    }

    @Override
    public ProductListResponseDto getProducts() {
        List<ProductResponseDto> respnseDtos = new ArrayList<>();
        for (Product p : repository.getAll()) {
            respnseDtos.add(new ProductResponseDto(p.getId(), p.getName(), priceToDto(p.getPrice()), new ImageDto(p.getImage().getUrl())));
        }
        return new ProductListResponseDto(respnseDtos);
    }

    @Override
    public void delete(String id) {
        assertProductExists(id);
        repository.delete(id);
    }

    private void assertRequestValid(ProductRequestDto productRequestDto) {
        if (!productRequestDto.isValid()) {
            throw new EmptyProductNameException("product name cannot be empty");
        }

        PriceDto price = productRequestDto.getPrice();
        if (price == null) {
            throw new EmptyProductNameException("product price cannot be empty");
        } else {
            if (price.getCurrency() == null) {
                throw new EmptyProductNameException("price.currency cannot be empty");
            }
            if (isNullOrBlank(price.getAmount())) {
                throw new EmptyProductNameException("price.amount cannot be empty");
            }

            if (!isValidNumber(price.getAmount())) {
                throw new EmptyProductNameException("price.amount is invalid");
            }
        }

        ImageDto image = productRequestDto.getImage();
        if (image == null) {
            throw new EmptyProductNameException("image is required");
        } else {
            if (image.getUrl() == null) {
                throw new EmptyProductNameException("image.url cannot be empty");
            }
        }
    }

    private void assertProductExists(String id) {
        if (repository.find(id) == null) {
            throw new ProductNotFoundException();
        }
    }

    private PriceDto priceToDto(Price price) {
        return new PriceDto(price.getAmount(), price.getCurrency());
    }

    private Price priceDtoToPrice(PriceDto dto) {
        return new Price(dto.getAmount(), dto.getCurrency());
    }

    private boolean isNullOrBlank(String string) {
        return string == null || string.isBlank();
    }

    private boolean isValidNumber(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
