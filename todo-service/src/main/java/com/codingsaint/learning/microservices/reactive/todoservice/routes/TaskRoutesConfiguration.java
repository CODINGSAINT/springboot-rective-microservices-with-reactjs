package com.codingsaint.learning.microservices.reactive.todoservice.routes;

import com.codingsaint.learning.microservices.reactive.todoservice.handler.TodoHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
@RequiredArgsConstructor
public class TaskRoutesConfiguration {

    private final TodoHandler todoHandler;

    @Bean
    public RouterFunction taskRouter() {

        return RouterFunctions.route(
                RequestPredicates.GET("/todos"), todoHandler::getAll)
                .andRoute(RequestPredicates.GET("/todos/{id}"), todoHandler::get)
                .andRoute(RequestPredicates.POST("/todos"), todoHandler::add)
                .andRoute(RequestPredicates.PUT("/todos"), todoHandler::update)
                .andRoute(RequestPredicates.DELETE("/todos/{id}"), todoHandler::delete)
                .andRoute(RequestPredicates.GET("/todos/user/{userId}"), todoHandler::findByUserId)

                ;
    }

}
