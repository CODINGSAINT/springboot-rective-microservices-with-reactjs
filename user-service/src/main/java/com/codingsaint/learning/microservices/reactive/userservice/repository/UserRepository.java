package com.codingsaint.learning.microservices.reactive.userservice.repository;

import com.codingsaint.learning.microservices.reactive.userservice.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends ReactiveMongoRepository<User,String> {
}
