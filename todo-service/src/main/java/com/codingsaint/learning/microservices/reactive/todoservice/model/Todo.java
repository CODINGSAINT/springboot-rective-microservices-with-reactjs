package com.codingsaint.learning.microservices.reactive.todoservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class Todo {
    private String id;
    private  String title;
    private String desc;
    private String userId;
}