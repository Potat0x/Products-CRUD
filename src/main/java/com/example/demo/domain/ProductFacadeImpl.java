package com.example.demo.domain;

import com.example.demo.domain.exceptions.InvalidDtoException;
import com.example.demo.infrastructure.ProductRepository;
import com.example.demo.infrastructure.exceptions.ProductNotFoundException;
import com.example.demo.infrastructure.exceptions.UnprocessableEntityException;
import org.springframework.stereotype.Component;

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

        return productToResponseDto(product);
    }

    @Override
    public ProductResponseDto find(String id) {
        assertProductExists(id);
        Product product = repository.find(id);
        return productToResponseDto(product);
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

        return productToResponseDto(updatedProduct);
    }

    @Override
    public ProductListResponseDto getProductsByTags(List<String> tags) {
        List<Product> fetchedProducts = (tags == null ? repository.getAll() : repository.getByTags(tags));
        return new ProductListResponseDto(fetchedProducts.stream()
                .map(this::productToResponseDto)
                .collect(Collectors.toList()));
    }

    @Override
    public void delete(String id) {
        assertProductExists(id);
        repository.delete(id);
    }

    private void assertRequestValid(ProductRequestDto productRequestDto) {
        try {
            productRequestDto.assertValid();
        } catch (InvalidDtoException e) {
            throw new UnprocessableEntityException(e.getMessage());
        }
    }

    private void assertProductExists(String id) {
        if (repository.find(id) == null) {
            throw new ProductNotFoundException();
        }
    }

    private ProductResponseDto productToResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                new PriceDto(product.getPrice().getAmount().toString(), product.getPrice().getCurrencyCode().toString()),
                new ImageDto(product.getImage().getUrl()),
                new DescriptionDto(product.getDescription().getText()),
                tagsToTagDtos(product.getTags())
        );
    }

    private Set<TagDto> tagsToTagDtos(Set<Tag> tags) {
        return tags.stream().map(tag -> new TagDto(tag.getName())).collect(Collectors.toSet());
    }

    private Price priceDtoToPrice(PriceDto dto) {
        return new Price(dto.getAmount(), CurrencyCode.valueOf(dto.getCurrency()));
    }

    private Set<Tag> tagDtosToTags(Set<TagDto> tagDtos) {
        return tagDtos.stream().map(tagDto -> new Tag(tagDto.getName())).collect(Collectors.toSet());
    }

}
