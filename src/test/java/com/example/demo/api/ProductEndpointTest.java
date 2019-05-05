package com.example.demo.api;

import com.example.demo.DemoApplicationTests;
import com.example.demo.domain.*;
import com.example.demo.infrastructure.exceptions.ProductNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductEndpointTest extends DemoApplicationTests {

    @Autowired
    ProductFacade productFacade;

    private final PriceDto dummyPrice = new PriceDto("99.99", "PLN");
    private final ImageDto dummyImg = new ImageDto("https://via.placeholder.com/150");
    private final DescriptionDto dummyDescr = new DescriptionDto("long description");
    private final Set<TagDto> dummyTags = Set.of(new TagDto("tag1"), new TagDto("tag2"));

    private String baseUrl() {
        return appUrl() + "/products";
    }

    @Test
    public void shouldCreateProduct() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("testname", dummyPrice, dummyImg, dummyDescr, dummyTags);

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);//201 created
        ProductResponseDto rdto = response.getBody();
        assertThat(rdto.getName()).isEqualTo("testname");
    }

    @Test
    public void shouldGet422DueToEmptyName() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("", dummyPrice, dummyImg, dummyDescr, dummyTags);

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldGet422DueToInvalidImageUrl() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("testname", dummyPrice, new ImageDto("invalid url"), dummyDescr, dummyTags);

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldGet422DueToInvalidCurrencyCode() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("testname", new PriceDto("123", "boom"), dummyImg, dummyDescr, dummyTags);

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldGet422DueToInvalidPriceAmount() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("testname", new PriceDto("boom", "PLN"), dummyImg, dummyDescr, dummyTags);

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldGet422DueToEmptyDescription() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("testname", dummyPrice, dummyImg, new DescriptionDto(""), dummyTags);

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldGetExistingProduct() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("name2", dummyPrice, dummyImg, dummyDescr, dummyTags);
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        String url = baseUrl() + "/" + existingProduct.getId();

        //when
        ResponseEntity<ProductResponseDto> result = httpClient.getForEntity(url, ProductResponseDto.class);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualToComparingFieldByField(existingProduct);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldGetExistingProductsByTags() {
        //given
        Arrays.asList(
                new ProductRequestDto("name1", dummyPrice, dummyImg, dummyDescr, tagDtoSetFromTagNames("tag1", "tag4")),
                new ProductRequestDto("name2", dummyPrice, dummyImg, dummyDescr, tagDtoSetFromTagNames("tag1", "tag2")),
                new ProductRequestDto("name3", dummyPrice, dummyImg, dummyDescr, tagDtoSetFromTagNames("tag1", "tag3, tag4")),
                new ProductRequestDto("name4", dummyPrice, dummyImg, dummyDescr, tagDtoSetFromTagNames("tag1", "tag4", "tag2"))
        ).forEach(requestDto -> productFacade.create(requestDto));

        String url = baseUrl() + "?tag=tag1&tag=tag2";
        Set<String> expectedProductsNames = Set.of("name2", "name4");

        //when
        ResponseEntity<ProductListResponseDto> result = httpClient.getForEntity(url, ProductListResponseDto.class);
        Set<String> fetchedProductsNames = result.getBody().getProducts().stream().map(ProductResponseDto::getName).collect(Collectors.toSet());

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(fetchedProductsNames.size()).isEqualTo(expectedProductsNames.size());
        assertThat(fetchedProductsNames).containsAll(expectedProductsNames);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldGetExistingProducts() {
        //given
        List<ProductResponseDto> existingProducts = new ArrayList<>();
        existingProducts.add(productFacade.create(new ProductRequestDto("name1", dummyPrice, dummyImg, dummyDescr, dummyTags)));
        existingProducts.add(productFacade.create(new ProductRequestDto("name2", dummyPrice, dummyImg, dummyDescr, dummyTags)));

        //when
        ResponseEntity<ProductListResponseDto> result = httpClient.getForEntity(baseUrl(), ProductListResponseDto.class);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);

        List<ProductResponseDto> products = result.getBody().getProducts();
        assertThat(products.size()).isEqualTo(existingProducts.size());
        assertThat(products.containsAll(existingProducts)).isTrue();
    }

    @Test
    public void shouldGet404() {
        //given
        String url = baseUrl() + "/boom";

        //when
        ResponseEntity<ProductResponseDto> result = httpClient.getForEntity(url, ProductResponseDto.class);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void shouldUpdateExistingProduct() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("old name", dummyPrice, dummyImg, dummyDescr, dummyTags);
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        String url = baseUrl() + "/" + existingProduct.getId();

        String newName = "updated name";
        ImageDto newImage = new ImageDto("https://via.placeholder.com/250");
        DescriptionDto newDescription = new DescriptionDto("updated description");
        PriceDto newPrice = new PriceDto("30000", "USD");
        Set<TagDto> newTags = Set.of(new TagDto("tag1"), new TagDto("tag22"), new TagDto("tag3"));

        //when
        String requestJson = mapToJson(new ProductRequestDto(newName, newPrice, newImage, newDescription, newTags));
        ResponseEntity<ProductResponseDto> reponse = httpClient.exchange(url, HttpMethod.PUT, getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(reponse.getStatusCodeValue()).isEqualTo(200);
        assertThat(reponse.getBody().getName()).isEqualTo(newName);
        assertThat(reponse.getBody().getImage()).isEqualTo(newImage);
        assertThat(reponse.getBody().getPrice()).isEqualTo(newPrice);
        assertThat(reponse.getBody().getDescription()).isEqualTo(newDescription);
        assertThat(reponse.getBody().getTags()).isEqualTo(newTags);
    }

    @Test(expected = ProductNotFoundException.class)
    public void shouldDeleteExistingProduct() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("name", dummyPrice, dummyImg, dummyDescr, dummyTags);
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        String url = baseUrl() + "/" + existingProduct.getId();

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
