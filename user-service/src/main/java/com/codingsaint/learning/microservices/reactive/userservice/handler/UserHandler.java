package com.codingsaint.learning.microservices.reactive.userservice.handler;

import com.codingsaint.learning.microservices.reactive.userservice.model.User;
import com.codingsaint.learning.microservices.reactive.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHandler {
    private final UserRepository userRepository;
    private final WebClient webClient;
    private final DiscoveryClient discoveryClient;
    private final ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    private static Mono<? extends ServerResponse> todosResponse(ClientResponse clientResponse) {
        return ServerResponse.status(clientResponse.statusCode())
                .headers(c -> clientResponse.headers().asHttpHeaders().forEach((name, value) ->
                        c.put(name, value)))
                .body(clientResponse.bodyToFlux(DataBuffer.class), DataBuffer.class);
    }


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
    public Mono<ServerResponse> userTasks(ServerRequest serverRequest){
        System.out.println(serverRequest.pathVariable("id"));

        /*List<ServiceInstance> instances = discoveryClient.getInstances("todo-service");
        ServiceInstance instance = instances.stream().findAny()
                .orElseThrow(() -> new IllegalStateException("No proxy instance available"));*/

        return
                reactiveCircuitBreakerFactory.create("userTodos").run(
                        webClient.get()
                        .uri("http://localhost:8080/todo-service/"
                                + "/todos/user/" + serverRequest.pathVariable("id"))
                        .exchange().flatMap(UserHandler::todosResponse),
                        throwable -> noTaskFound());
    }
    private Mono<ServerResponse> noTaskFound(){
        System.out.println("Some Issue ");
    return ServerResponse.ok().body(BodyInserters.fromValue("No Todos Found, Create New"));
    }
}
