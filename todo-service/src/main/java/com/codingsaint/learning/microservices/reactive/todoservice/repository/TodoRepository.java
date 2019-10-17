package com.codingsaint.learning.microservices.reactive.todoservice.repository;

import com.codingsaint.learning.microservices.reactive.todoservice.model.Todo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TodoRepository extends ReactiveMongoRepository<Todo,String> {
    Flux<Todo> findByUserId(String userId);
}
