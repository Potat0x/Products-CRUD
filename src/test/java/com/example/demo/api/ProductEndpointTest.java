package com.example.demo.api;

import com.example.demo.DemoApplicationTests;
import com.example.demo.domain.*;
import com.example.demo.infrastructure.exceptions.ProductNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductEndpointTest extends DemoApplicationTests {

    @Autowired
    ProductFacade productFacade;

    private final PriceDto dummyPrice = new PriceDto("99.99", "PLN");
    private final ImageDto dummyImage = new ImageDto("https://via.placeholder.com/150");
    private final DescriptionDto dummyDescription = new DescriptionDto("long description");
    private final Set<TagDto> dummyTags = Set.of(new TagDto("tag1"), new TagDto("tag2"));

    @Test
    public void shouldCreateProduct() throws URISyntaxException {
        //given
        final String url = "http://localhost:" + port + "/products";

        //when
        ProductRequestDto requestDto = new ProductRequestDto("testname", dummyPrice, dummyImage, dummyDescription, dummyTags);
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(url, getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);//201 created
        ProductResponseDto rdto = response.getBody();
        Assertions.assertThat(rdto.getName()).isEqualTo("testname");
    }

    @Test
    public void shouldGetExistingProduct() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("name2", dummyPrice, dummyImage, dummyDescription, dummyTags);
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        final String url = "http://localhost:" + port + "/products/" + existingProduct.getId();

        //when
        ResponseEntity<ProductResponseDto> result = httpClient.getForEntity(url, ProductResponseDto.class);

        //then
        Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(result.getBody()).isEqualToComparingFieldByField(existingProduct);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldGetExistingProductsByTags() {
        //given
        Set<TagDto> tags1 = tagDtoSetFromTagNames("tag1", "tag4");
        Set<TagDto> tags2 = tagDtoSetFromTagNames("tag1", "tag2");
        Set<TagDto> tags3 = tagDtoSetFromTagNames("tag1", "tag3, tag4");
        Set<TagDto> tags4 = tagDtoSetFromTagNames("tag1", "tag4", "tag2");

        List<ProductRequestDto> productsToCreateDtos = new ArrayList<>();
        productsToCreateDtos.add(new ProductRequestDto("name1", dummyPrice, dummyImage, dummyDescription, tags1));
        productsToCreateDtos.add(new ProductRequestDto("name2", dummyPrice, dummyImage, dummyDescription, tags2));
        productsToCreateDtos.add(new ProductRequestDto("name3", dummyPrice, dummyImage, dummyDescription, tags3));
        productsToCreateDtos.add(new ProductRequestDto("name4", dummyPrice, dummyImage, dummyDescription, tags4));

        for (ProductRequestDto requestDto : productsToCreateDtos) {
            productFacade.create(requestDto);
        }

        final String url = "http://localhost:" + port + "/products?tag=tag1&tag=tag2";

        Set<Set<TagDto>> expectedTags = Set.of(tags2, tags4);
        //when
        ResponseEntity<ProductListResponseDto> result = httpClient.getForEntity(url, ProductListResponseDto.class);
        Set<Set<TagDto>> fetchedProductsTags = result.getBody().getProducts().stream().map(ProductResponseDto::getTags).collect(Collectors.toSet());

        //then
        Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(fetchedProductsTags.size()).isEqualTo(expectedTags.size());
        Assertions.assertThat(fetchedProductsTags).containsAll(expectedTags);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldGetExistingProductList() {
        //given
        List<ProductResponseDto> existingProducts = new ArrayList<>();
        existingProducts.add(productFacade.create(new ProductRequestDto("name1", dummyPrice, dummyImage, dummyDescription, dummyTags)));
        existingProducts.add(productFacade.create(new ProductRequestDto("name2", dummyPrice, dummyImage, dummyDescription, dummyTags)));
        final String url = "http://localhost:" + port + "/products/";

        //when
        ResponseEntity<ProductListResponseDto> result = httpClient.getForEntity(url, ProductListResponseDto.class);

        //then
        Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(200);

        List<ProductResponseDto> products = result.getBody().getProducts();
        Assertions.assertThat(products.containsAll(existingProducts)).isTrue();
    }

    @Test
    public void shouldGet404() {
        //given
        final String url = "http://localhost:" + port + "/products/nonexistentid";

        //when
        ResponseEntity<ProductResponseDto> result = httpClient.getForEntity(url, ProductResponseDto.class);

        //then
        Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void shouldUpdateExistingProduct() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("old name", dummyPrice, dummyImage, dummyDescription, dummyTags);
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        final String url = "http://localhost:" + port + "/products/" + existingProduct.getId();
        final String newName = "updated name";
        final ImageDto newImage = new ImageDto("https://via.placeholder.com/250");
        final DescriptionDto newDescription = new DescriptionDto("updated description");
        final PriceDto newPrice = new PriceDto("30000", "USD");
        final Set<TagDto> newTags = Set.of(new TagDto("tag1"), new TagDto("tag22"), new TagDto("tag3"));

        String requestJson = mapToJson(new ProductRequestDto(newName, newPrice, newImage, newDescription, newTags));

        //when
        ResponseEntity<ProductResponseDto> reponse =
                httpClient.exchange(url, HttpMethod.PUT, getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        Assertions.assertThat(reponse.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(reponse.getBody().getName()).isEqualTo(newName);
        Assertions.assertThat(reponse.getBody().getImage()).isEqualTo(newImage);
        Assertions.assertThat(reponse.getBody().getPrice()).isEqualTo(newPrice);
        Assertions.assertThat(reponse.getBody().getDescription()).isEqualTo(newDescription);
        Assertions.assertThat(reponse.getBody().getTags()).isEqualTo(newTags);
    }

    @Test(expected = ProductNotFoundException.class)
    public void shouldDeleteExistingProduct() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("name", dummyPrice, dummyImage, dummyDescription, dummyTags);
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        final String url = "http://localhost:" + port + "/products/" + existingProduct.getId();

        //when
        httpClient.delete(url);

        //then
        productFacade.find(existingProduct.getId());
    }

    private HttpEntity<String> getHttpRequest(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return new HttpEntity<>(json, headers);
    }

    private String mapToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<TagDto> tagDtoSetFromTagNames(String... names) {
        Set<TagDto> tagSet = new HashSet<>();
        for (String name : names) {
            tagSet.add(new TagDto(name));
        }
        return tagSet;
    }
}
