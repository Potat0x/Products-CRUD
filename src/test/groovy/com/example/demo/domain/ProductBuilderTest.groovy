package com.example.demo.domain

import spock.lang.Specification

import com.example.demo.domain.exceptions.ProductBuilderException

class ProductBuilderTest extends Specification {

    def "should thrown exception"() {
        given: "builder with uninitialized fields"
        def builder = new ProductBuilder().setId("id").setName("name")

        when: "building Product using uninitialized builder"
        builder.build()

        then:
        thrown(ProductBuilderException)
    }
}
