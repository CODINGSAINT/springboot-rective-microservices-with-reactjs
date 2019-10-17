package com.codingsaint.learning.microservices.reactive.userservice.routes;

import com.codingsaint.learning.microservices.reactive.userservice.handler.UserHandler;
import com.codingsaint.learning.microservices.reactive.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
@RequiredArgsConstructor
public class UserRouteConfiguration {
    private final UserRepository userRepository;
    private final UserHandler userHandler;

    @Bean
    public RouterFunction userRoutes() {
        return RouterFunctions.route(
                RequestPredicates.GET("/users"), userHandler::getAll)
                .andRoute(RequestPredicates.POST("/user"), userHandler::add)
                .andRoute(RequestPredicates.PUT("/user"), userHandler::update)
                .andRoute(RequestPredicates.GET("/user/{id}"), userHandler::get)
                .andRoute(RequestPredicates.DELETE("/user/{id}"), userHandler::delete)
                .andRoute(RequestPredicates.GET("/user/{id}/todos"), userHandler::userTasks);

    }
}