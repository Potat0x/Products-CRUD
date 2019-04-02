package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class HelloWorldEndpointTest extends DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void shouldReturnGreetings() {
        //given
        final String url = "http://localhost:" + port + "/hello";
        //when
        ResponseEntity<String> response = httpClient.getForEntity(url, String.class);//kb shortcut
        //then
        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).isEqualTo("hello heroku world");
    }
}
