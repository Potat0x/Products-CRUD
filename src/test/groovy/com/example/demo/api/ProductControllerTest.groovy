package com.example.demo.api

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import com.example.demo.domain.DescriptionDto
import com.example.demo.domain.ImageDto
import com.example.demo.domain.PriceDto
import com.example.demo.domain.ProductFacade
import com.example.demo.domain.ProductRequestDto
import com.example.demo.domain.ProductResponseDto
import com.example.demo.domain.TagDto

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest extends Specification {

    @Autowired
    ProductFacade productFacade

    @Autowired
    TestRestTemplate httpClient

    @LocalServerPort
    int port

    @Autowired
    ObjectMapper objectMapper

    PriceDto dummyPrice = new PriceDto("99.99", "PLN")
    ImageDto dummyImg = new ImageDto("https://via.placeholder.com/150")
    DescriptionDto dummyDescr = new DescriptionDto("long description")
    Set<TagDto> dummyTags = Set.of(new TagDto("tag1"))


    def "should create product"() {
        given: "Valid product request DTO"
        def requestDto = new ProductRequestDto("testname", dummyPrice, dummyImg, dummyDescr, dummyTags)
        def expectedResponseDto = new ProductResponseDto("id", "testname", dummyPrice, dummyImg, dummyDescr, dummyTags)

        def jsonGenerator = new JsonGenerator.Options().excludeFieldsByName("id").build()
        when: "Creating product with POST request"
        String requestJson = mapToJson(requestDto)
        ResponseEntity<ProductResponseDto> response = httpClient.postForEntity(productsUrl(), getHttpRequest(requestJson), ProductResponseDto.class)

        then: "Created product should be returned with status 201"
        assert response.getStatusCodeValue() == 201
        assert jsonGenerator.toJson(response.getBody()) == jsonGenerator.toJson(expectedResponseDto)
    }

    def "Should get product"() {
        given: "ID of existing product"
        def requestDto = new ProductRequestDto("name2", dummyPrice, dummyImg, dummyDescr, dummyTags)
        def existingProduct = productFacade.create(requestDto)
        def url = "${productsUrl()}/${existingProduct.getId()}"

        when: "Request for product by ID"
        ResponseEntity<ProductResponseDto> response = httpClient.getForEntity(url, ProductResponseDto.class)

        then: "Product should be returned"
        assert response.getStatusCodeValue() == 200
        assert response.getBody() == existingProduct
    }


    private HttpEntity<String> getHttpRequest(String json) {
        HttpHeaders headers = new HttpHeaders()
        headers.set("Content-Type", "application/json")
        return new HttpEntity<>(json, headers)
    }

    private String mapToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj)
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e)
        }
    }

    private String productsUrl() {
        return "http://localhost:" + port + "/products"
    }
}
