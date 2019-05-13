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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductEndpointTest extends DemoApplicationTests {

    @Autowired
    ProductFacade productFacade;

    @Test
    public void shouldCreateProduct() {
        //given
        ProductRequestDto requestDto = validDto().build();

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        ProductResponseDto rdto = response.getBody();
        assertThat(rdto.getName()).isEqualTo("testname"); //only name checking here, better implementation in Spock tests
    }

    @Test
    public void shouldReceive422DueToEmptyName() {
        //given
        ProductRequestDto requestDto = validDto().setName("").build();

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldReceive422DueToEmptyTagName() {
        //given
        ProductRequestDto requestDto = validDto().setTags(tagDtoSetFromTagNames("", "tag")).build();

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldReceive422DueToEmptyTagList() {
        //given
        ProductRequestDto requestDto = validDto().setTags(tagDtoSetFromTagNames()).build();

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldReceive422DueToInvalidImageUrl() {
        //given
        ProductRequestDto requestDto = validDto().setImage(new ImageDto("invalid url")).build();

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldReceive422DueToInvalidCurrencyCode() {
        //given
        ProductRequestDto requestDto = validDto().setPrice(new PriceDto("123", "boom")).build();

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldReceive422DueToInvalidPriceAmount() {
        //given
        ProductRequestDto requestDto = validDto().setPrice(new PriceDto("boom", "PLN")).build();

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldReceive422DueToEmptyDescription() {
        //given
        ProductRequestDto requestDto = validDto().setDescription(new DescriptionDto("")).build();

        //when
        String requestJson = mapToJson(requestDto);
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(baseUrl(), getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(422);
    }

    @Test
    public void shouldGetExistingProduct() {
        //given
        ProductRequestDto requestDto = validDto().setName("name2").build();
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
                validDto().setName("name1").setTags(tagDtoSetFromTagNames("tag1", "tag4")),
                validDto().setName("name2").setTags(tagDtoSetFromTagNames("tag1", "tag2")),
                validDto().setName("name3").setTags(tagDtoSetFromTagNames("tag1", "tag3, tag4")),
                validDto().setName("name4").setTags(tagDtoSetFromTagNames("tag1", "tag4", "tag2"))
        ).forEach(requestDtoBuilder -> productFacade.create(requestDtoBuilder.build()));

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
        existingProducts.add(productFacade.create(validDto().setName("name1").build()));
        existingProducts.add(productFacade.create(validDto().setName("name1").build()));

        //when
        ResponseEntity<ProductListResponseDto> result = httpClient.getForEntity(baseUrl(), ProductListResponseDto.class);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);

        List<ProductResponseDto> products = result.getBody().getProducts();
        assertThat(products.size()).isEqualTo(existingProducts.size());
        assertThat(products.containsAll(existingProducts)).isTrue();
    }

    @Test
    public void shouldReceive404() {
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
        ProductRequestDto requestDto = validDto().setName("old name").build();
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
        ProductRequestDto requestDto = validDto().build();
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        String url = baseUrl() + "/" + existingProduct.getId();

        //when
        httpClient.delete(url);

        //then
        productFacade.find(existingProduct.getId());
    }

    private ProductRequestDtoBuilder validDto() {
        return new ProductRequestDtoBuilder()
                .setName("testname")
                .setPrice(new PriceDto("99.99", "PLN"))
                .setImage(new ImageDto("https://via.placeholder.com/150"))
                .setDescription(new DescriptionDto("long description"))
                .setTags(Set.of(new TagDto("tag1"), new TagDto("tag2")));
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
        return Stream.of(names).map(TagDto::new).collect(Collectors.toSet());
    }

    private String baseUrl() {
        return appUrl() + "/products";
    }
}
