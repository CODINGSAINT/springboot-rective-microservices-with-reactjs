package com.codingsaint.learning.microservices.reactive.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class ProxyConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder){
      return builder.routes()
                .route("users_service_route",
                        route -> route.path("/user-service/**")
                                //.and()
                               // .method(HttpMethod.POST)
                                .filters(filter -> filter.stripPrefix(1)
                                )
                                .uri("lb://user-service"))
              .route("todo_service_route",
                route -> route.path("/todo-service/**")
                        //.and()
                        // .method(HttpMethod.POST)
                        .filters(filter -> filter.stripPrefix(1)
                        )
                        .uri("lb://todo-service"))
              .build();
    }
}
