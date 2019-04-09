package com.example.demo.api;

import com.example.demo.DemoApplicationTests;
import com.example.demo.domain.ProductFacade;
import com.example.demo.domain.ProductRequestDto;
import com.example.demo.domain.ProductResponseDto;
import com.example.demo.domain.ProductListResponseDto;
import com.example.demo.infrastructure.exceptions.ProductNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ProductEndpointTest extends DemoApplicationTests {

    @Autowired
    ProductFacade productFacade;

    @Test
    public void shouldCreateProduct() throws URISyntaxException {
        //given
        final String url = "http://localhost:" + port + "/products";

        //when
        ProductRequestDto requestDto = new ProductRequestDto("testname");
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
        ProductRequestDto requestDto = new ProductRequestDto("name2");
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
    public void shouldGetExistingProductList() {
        //given
        List<ProductResponseDto> existingProducts = new ArrayList<>();
        existingProducts.add(productFacade.create(new ProductRequestDto("name1")));
        existingProducts.add(productFacade.create(new ProductRequestDto("name2")));
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
        ProductRequestDto requestDto = new ProductRequestDto("old name");
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        final String url = "http://localhost:" + port + "/products/" + existingProduct.getId();
        final String newName = "updated name";
        String requestJson = mapToJson(new ProductRequestDto(newName));

        //when
        ResponseEntity<ProductResponseDto> reponse =
                httpClient.exchange(url, HttpMethod.PUT, getHttpRequest(requestJson), ProductResponseDto.class);

        //then
        Assertions.assertThat(reponse.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(reponse.getBody().getName()).isEqualTo(newName);
    }

    @Test(expected = ProductNotFoundException.class)
    public void shouldDeleteExistingProduct() {
        //given
        ProductRequestDto requestDto = new ProductRequestDto("name");
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
}
