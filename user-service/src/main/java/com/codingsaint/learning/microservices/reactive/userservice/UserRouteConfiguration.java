package com.codingsaint.learning.microservices.reactive.userservice;

import com.codingsaint.learning.microservices.reactive.userservice.model.User;
import com.codingsaint.learning.microservices.reactive.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class UserRouteConfiguration {
    private final UserRepository userRepository;

    @Bean
    public RouterFunction userRoutes() {
        return RouterFunctions.route(
                RequestPredicates.GET("/users"), serverRequest ->
                        ServerResponse.ok()
                                .body(BodyInserters.fromPublisher(
                                        userRepository.findAll(), User.class))
        ).andRoute(RequestPredicates.POST("/user"), serverRequest -> {
            Mono<User> user = serverRequest.bodyToMono(User.class);
            return ServerResponse.status(HttpStatus.CREATED)
                    .body(BodyInserters.fromPublisher(
                            user.flatMap(userRepository::save), User.class)
                    );
        }).andRoute(RequestPredicates.PUT("/user"), serverRequest -> {
            Mono<User> user = serverRequest.bodyToMono(User.class);
            return ServerResponse.status(HttpStatus.OK)
                    .body(BodyInserters.fromPublisher(
                            user.flatMap(userRepository::save), User.class)
                    );
        }).andRoute(RequestPredicates.GET("/user/{id}"), serverRequest -> {
            String _id = serverRequest.pathVariable("id");
            return ServerResponse.ok()
                    .body(BodyInserters.fromPublisher(
                            userRepository.findById(_id), User.class));
        }).andRoute(RequestPredicates.DELETE("/user/{id}"), serverRequest -> {
            String _id = serverRequest.pathVariable("id");
            return ServerResponse.noContent().build(userRepository.deleteById(_id));
        });
    }
}
