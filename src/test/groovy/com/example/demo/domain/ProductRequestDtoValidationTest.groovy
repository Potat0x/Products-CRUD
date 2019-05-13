package com.example.demo.domain

import com.example.demo.domain.DescriptionDto
import com.example.demo.domain.ImageDto
import com.example.demo.domain.PriceDto
import com.example.demo.domain.ProductRequestDtoBuilder
import com.example.demo.domain.TagDto
import com.example.demo.domain.exceptions.InvalidDtoException
import spock.lang.Specification
import spock.lang.Unroll

class ProductRequestDtoValidationTest extends Specification {

    @Unroll
    def "should throw exception if DTO invalid: #productRequestDtoBuilder.build()"() {
        expect:
        def exceptionThrown = false
        try {
            productRequestDtoBuilder.build().assertValid()
        } catch (InvalidDtoException ignored) {
            exceptionThrown = true
        } catch (Exception e) {
            e.printStackTrace()
            throw new RuntimeException("unexpected exception")
        }

        exceptionThrown == shouldExceptionBeThrown

        where:
        productRequestDtoBuilder                                      | shouldExceptionBeThrown
        validDto()                                                    | false
        validDto().setName(null)                                      | true
        validDto().setName("")                                        | true
        validDto().setPrice(null)                                     | true
        validDto().setPrice(new PriceDto("123", "invalid currency"))  | true
        validDto().setPrice(new PriceDto("invalid amount", "PLN"))    | true
        validDto().setPrice(new PriceDto("-123", "PLN"))              | true
        validDto().setImage(null)                                     | true
        validDto().setImage(new ImageDto(null))                       | true
        validDto().setImage(new ImageDto(""))                         | true
        validDto().setImage(new ImageDto("invalid url"))              | true
        validDto().setDescription(null)                               | true
        validDto().setDescription(new DescriptionDto(null))           | true
        validDto().setDescription(new DescriptionDto(""))             | true
        validDto().setTags(null)                                      | true
        validDto().setTags(Set.of())                                  | true
        validDto().setTags(Set.of(new TagDto()))                      | true
        validDto().setTags(Set.of(new TagDto("")))                    | true
        validDto().setTags(Set.of(new TagDto(), new TagDto("tag")))   | true
        validDto().setTags(Set.of(new TagDto(""), new TagDto("tag"))) | true
    }

    private def validDto() {
        def dummyPrice = new PriceDto("99.99", "PLN")
        def dummyImg = new ImageDto("https://via.placeholder.com/150")
        def dummyDescr = new DescriptionDto("long description")
        def dummyTags = Set.of(new TagDto("tag1"))
        return new ProductRequestDtoBuilder()
                .setName("name")
                .setDescription(dummyDescr)
                .setImage(dummyImg)
                .setTags(dummyTags)
                .setPrice(dummyPrice)
    }
}
