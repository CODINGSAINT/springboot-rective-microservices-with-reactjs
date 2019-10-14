package com.codingsaint.learning.microservices.reactive.todoservice.repository;

import com.codingsaint.learning.microservices.reactive.todoservice.model.Todo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends ReactiveMongoRepository<Todo,String> {
}
