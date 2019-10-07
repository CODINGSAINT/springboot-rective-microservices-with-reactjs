package com.codingsaint.learning.microservices.reactive.userservice.handler;

import com.codingsaint.learning.microservices.reactive.userservice.model.User;
import com.codingsaint.learning.microservices.reactive.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserHandler {
    private final UserRepository userRepository;

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        Mono<User> user = serverRequest.bodyToMono(User.class);
        return ServerResponse.ok()
                .body(BodyInserters.fromPublisher(
                        userRepository.findAll(), User.class));
    }

    public Mono<ServerResponse> add(ServerRequest serverRequest) {
        Mono<User> user = serverRequest.bodyToMono(User.class);
        return ServerResponse.status(HttpStatus.CREATED)
                .body(BodyInserters.fromPublisher(
                        user.flatMap(userRepository::save), User.class));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        Mono<User> user = serverRequest.bodyToMono(User.class);
        return ServerResponse.status(HttpStatus.OK)
                .body(BodyInserters.fromPublisher(
                        user.flatMap(userRepository::save), User.class));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String _id = serverRequest.pathVariable("id");
        return ServerResponse.noContent().build(userRepository.deleteById(_id));
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        String _id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .body(BodyInserters.fromPublisher(
                        userRepository.findById(_id), User.class));
    }
}
