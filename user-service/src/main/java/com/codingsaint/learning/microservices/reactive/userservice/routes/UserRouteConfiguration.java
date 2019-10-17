package com.codingsaint.learning.microservices.reactive.userservice.routes;

import com.codingsaint.learning.microservices.reactive.userservice.handler.UserHandler;
import com.codingsaint.learning.microservices.reactive.userservice.model.User;
import com.codingsaint.learning.microservices.reactive.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UserRouteConfiguration {
    private final UserRepository userRepository;
    private final UserHandler userHandler;
    private final WebClient webClient;

    private final DiscoveryClient discoveryClient;

    @Bean
    public RouterFunction userRoutes() {
        return RouterFunctions.route(
                RequestPredicates.GET("/users"), userHandler::getAll)
                .andRoute(RequestPredicates.POST("/user"), userHandler::add)
                .andRoute(RequestPredicates.PUT("/user"), userHandler::update)
                .andRoute(RequestPredicates.GET("/user/{id}"), userHandler::get)
                .andRoute(RequestPredicates.DELETE("/user/{id}"), userHandler::delete)
                .andRoute(RequestPredicates.GET("/user/{id}/todos"),serverRequest -> {
                    System.out.println(serverRequest.pathVariable("id"));
                    List<ServiceInstance> instances = discoveryClient.getInstances("todo-service");
                    ServiceInstance instance = instances.stream().findAny()
                            .orElseThrow(() -> new IllegalStateException("No proxy instance available"));
                    System.out.println(instance.getUri().toString());

                    return  webClient.get()
                            .uri( instance.getUri().toString()+"/todos/user/"+serverRequest.pathVariable("id"))
                            .exchange().flatMap((ClientResponse clientResponse)->{
                        return ServerResponse.status(clientResponse.statusCode())
                                .headers(c -> clientResponse.headers().asHttpHeaders().forEach((name, value) ->
                                        c.put(name, value)))
                                .body(clientResponse.bodyToFlux(DataBuffer.class), DataBuffer.class);
                    });
                });
    }

}
