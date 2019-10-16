package com.codingsaint.learning.microservices.reactive.userservice.routes;

import com.codingsaint.learning.microservices.reactive.userservice.handler.UserHandler;
import com.codingsaint.learning.microservices.reactive.userservice.model.User;
import com.codingsaint.learning.microservices.reactive.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class UserRouteConfiguration {
    private final UserRepository userRepository;
    private final UserHandler userHandler;
    private final WebClient webClient;

    @Bean
    public RouterFunction userRoutes() {
        return RouterFunctions.route(
                RequestPredicates.GET("/users"), userHandler::getAll)
                .andRoute(RequestPredicates.POST("/user"), userHandler::add)
                .andRoute(RequestPredicates.PUT("/user"), userHandler::update)
                .andRoute(RequestPredicates.GET("/user/{id}"), userHandler::get)
                .andRoute(RequestPredicates.DELETE("/user/{id}"), userHandler::delete)
                /*.andRoute(RequestPredicates.GET("user/{id}/tasks"),serverRequest -> {
                    webClient.get()
                            .uri("lb://task-service/tasks/user/"+serverRequest.pathVariable("id"))
                })*/;
    }

}
