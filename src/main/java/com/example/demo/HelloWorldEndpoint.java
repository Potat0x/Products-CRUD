package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloWorldEndpoint {
    @RequestMapping(method= RequestMethod.GET, path = "/hello")
    String hello(){
        return "hello heroku world";
    }
}
