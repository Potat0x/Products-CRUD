package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldEndpoint {
    @RequestMapping(method= RequestMethod.GET, path = "/hello")
//    @GetMapping("/hello") //==jw
    String hello(){
        return "hello heroku world";
    }
}
