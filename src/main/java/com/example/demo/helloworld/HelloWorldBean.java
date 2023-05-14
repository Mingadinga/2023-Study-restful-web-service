package com.example.demo.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // getter, setter, constructor, toString, equals
@AllArgsConstructor
public class HelloWorldBean {

    private String message;

}
