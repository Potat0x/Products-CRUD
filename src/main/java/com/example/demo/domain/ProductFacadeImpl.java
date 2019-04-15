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
        Product product = new Product(id, requestDto.getName(), now, priceDtoToPrice(requestDto.getPrice()), new Image(requestDto.getImage().getUrl()), new Description(requestDto.getDescription().getText()));

        repository.save(product);
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(), product.getName(), priceToDto(product.getPrice()), requestDto.getImage(), requestDto.getDescription());
        return productResponseDto;
    }

    @Override
    public ProductResponseDto find(String id) {
        assertProductExists(id);
        Product product = repository.find(id);
        return new ProductResponseDto(product.getId(), product.getName(), priceToDto(product.getPrice()), new ImageDto(product.getImage().getUrl()), new DescriptionDto(product.getDescription().getText()));
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto requestDto) {
        assertProductExists(id);
        assertRequestValid(requestDto);

        Product product = repository.find(id);

        PriceDto newPriceDto = requestDto.getPrice();
        Price newPrice = new Price(newPriceDto.getAmount(), newPriceDto.getCurrency());

        Product updatedProduct = repository.update(id, product.withNewName(requestDto.getName()).withNewImage(new Image(requestDto.getImage().getUrl())).withNewDescription(new Description(requestDto.getDescription().getText())).withNewPrice(newPrice));
        System.out.println(requestDto.getImage());
        System.out.println(product.withNewImage(new Image(requestDto.getImage().getUrl())));
        System.out.println(updatedProduct);
        return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName(), priceToDto(updatedProduct.getPrice()), new ImageDto(updatedProduct.getImage().getUrl()), new DescriptionDto(updatedProduct.getDescription().getText()));
    }

    @Override
    public ProductListResponseDto getProducts() {
        List<ProductResponseDto> respnseDtos = new ArrayList<>();
        for (Product p : repository.getAll()) {
            respnseDtos.add(new ProductResponseDto(p.getId(), p.getName(), priceToDto(p.getPrice()), new ImageDto(p.getImage().getUrl()), new DescriptionDto(p.getDescription().getText())));
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

        DescriptionDto description = productRequestDto.getDescription();
        if (description == null) {
            throw new EmptyProductNameException("description is required");
        } else {
            if (description.getText() == null) {
                throw new EmptyProductNameException("description.text cannot be empty");
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
