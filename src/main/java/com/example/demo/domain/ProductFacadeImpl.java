package com.example.demo.domain;

import com.example.demo.infrastructure.ProductRepository;
import com.example.demo.infrastructure.exceptions.ProductNotFoundException;
import com.example.demo.infrastructure.exceptions.UnprocessableEntityException;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Product product = new Product(id, requestDto.getName(), LocalDateTime.now(),
                priceDtoToPrice(requestDto.getPrice()),
                new Image(requestDto.getImage().getUrl()),
                new Description(requestDto.getDescription().getText()),
                tagDtosToTags(requestDto.getTags()));

        repository.save(product);

        ImageDto imageDto = new ImageDto(product.getImage().getUrl());
        DescriptionDto descriptionDto = new DescriptionDto(product.getDescription().getText());
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                priceToDto(product.getPrice()),
                imageDto, descriptionDto,
                tagsToTagDtos(product.getTags())
        );
    }

    @Override
    public ProductResponseDto find(String id) {
        assertProductExists(id);
        Product product = repository.find(id);
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                priceToDto(product.getPrice()),
                new ImageDto(product.getImage().getUrl()),
                new DescriptionDto(product.getDescription().getText()),
                tagsToTagDtos(product.getTags())
        );
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto requestDto) {
        assertProductExists(id);
        assertRequestValid(requestDto);

        Product product = repository.find(id);

        PriceDto newPriceDto = requestDto.getPrice();
        Price newPrice = new Price(newPriceDto.getAmount(), CurrencyCode.valueOf(newPriceDto.getCurrency()));
        Set<Tag> newTags = tagDtosToTags(requestDto.getTags());

        Product updatedProduct = repository.update(id,
                product.withNewName(requestDto.getName())
                        .withNewImage(new Image(requestDto.getImage().getUrl()))
                        .withNewDescription(new Description(requestDto.getDescription().getText()))
                        .withNewPrice(newPrice).withNewTags(newTags)
        );

        return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName(), priceToDto(updatedProduct.getPrice()), new ImageDto(updatedProduct.getImage().getUrl()), new DescriptionDto(updatedProduct.getDescription().getText()), tagsToTagDtos(updatedProduct.getTags()));
    }

    @Override
    public ProductListResponseDto getProducts(List<String> tags) {
        List<Product> fetchedProducts = (tags == null ? repository.getAll() : repository.getByTags(tags));
        return new ProductListResponseDto(fetchedProducts.stream()
                .map(p -> new ProductResponseDto(
                        p.getId(),
                        p.getName(),
                        priceToDto(p.getPrice()),
                        new ImageDto(p.getImage().getUrl()),
                        new DescriptionDto(p.getDescription().getText()),
                        tagsToTagDtos(p.getTags()))
                )
                .collect(Collectors.toList()));
    }

    @Override
    public void delete(String id) {
        assertProductExists(id);
        repository.delete(id);
    }

    private void assertRequestValid(ProductRequestDto productRequestDto) {
        if (!productRequestDto.isValid()) {
            throw new UnprocessableEntityException("product name cannot be empty");
        }

        PriceDto price = productRequestDto.getPrice();
        if (price == null) {
            throw new UnprocessableEntityException("product price cannot be empty");
        } else {
            if (price.getCurrency() == null) {
                throw new UnprocessableEntityException("price.currency cannot be empty");
            } else {
                try {
                    CurrencyCode.valueOf(price.getCurrency());
                } catch (IllegalArgumentException e) {
                    throw new UnprocessableEntityException("invalid price.currency");
                }
            }
            if (Strings.isNullOrEmpty(price.getAmount())) {
                throw new UnprocessableEntityException("price.amount cannot be empty");
            }

            if (!isValidNumber(price.getAmount())) {
                throw new UnprocessableEntityException("invalid price.amount");
            }
        }

        ImageDto image = productRequestDto.getImage();
        if (image == null) {
            throw new UnprocessableEntityException("image is required");
        } else {
            if (image.getUrl() == null) {
                throw new UnprocessableEntityException("image.url cannot be empty");
            }

            try {
                new URL(image.getUrl());
            } catch (MalformedURLException e) {
                throw new UnprocessableEntityException("invalid image.url");
            }
        }

        DescriptionDto description = productRequestDto.getDescription();
        if (description == null) {
            throw new UnprocessableEntityException("description is required");
        } else {
            if (Strings.isNullOrEmpty(description.getText())) {
                throw new UnprocessableEntityException("description.text cannot be empty");
            }
        }

        Set<TagDto> tagDtos = productRequestDto.getTags();
        if (tagDtos == null) {
            throw new UnprocessableEntityException("tags are required");
        } else {
            if (tagDtos.stream().anyMatch(tagDto -> tagDto.getName() == null)) {
                throw new UnprocessableEntityException("tag.name cannot be empty");
            }
        }
    }

    private void assertProductExists(String id) {
        if (repository.find(id) == null) {
            throw new ProductNotFoundException();
        }
    }

    private PriceDto priceToDto(Price price) {
        return new PriceDto(price.getAmount().toString(), price.getCurrencyCode().toString());
    }

    private Price priceDtoToPrice(PriceDto dto) {
        return new Price(dto.getAmount(), CurrencyCode.valueOf(dto.getCurrency()));
    }

    private Set<TagDto> tagsToTagDtos(Set<Tag> tags) {
        return tags.stream().map(tag -> new TagDto(tag.getName())).collect(Collectors.toSet());
    }

    private Set<Tag> tagDtosToTags(Set<TagDto> tagDtos) {
        return tagDtos.stream().map(tagDto -> new Tag(tagDto.getName())).collect(Collectors.toSet());
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
