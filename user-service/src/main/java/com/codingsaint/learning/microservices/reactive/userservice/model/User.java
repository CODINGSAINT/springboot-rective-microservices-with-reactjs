package com.codingsaint.learning.microservices.reactive.userservice.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {
    private String id;
    private  String name;
    private String email;
}
