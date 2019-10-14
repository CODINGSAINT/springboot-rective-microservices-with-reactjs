package com.codingsaint.learning.microservices.reactive.todoservice.handler;

import com.codingsaint.learning.microservices.reactive.todoservice.model.Todo;
import com.codingsaint.learning.microservices.reactive.todoservice.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TodoHandler {

    private final TodoRepository todoRepository;

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(BodyInserters.fromPublisher(
                        todoRepository.findAll(), Todo.class
                ));
    }
    public Mono<ServerResponse> get(ServerRequest serverRequest){
        String _id=serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .body(BodyInserters.fromPublisher(todoRepository.findById(_id),Todo.class)
                       );
    }

    public Mono<ServerResponse> add(ServerRequest serverRequest){
        return ServerResponse.status(HttpStatus.CREATED)
                .body(
                BodyInserters.fromPublisher(
                        serverRequest.bodyToMono(Todo.class).flatMap(todoRepository::save),Todo.class)
        );
    }
    public Mono<ServerResponse> update(ServerRequest serverRequest){
        return ServerResponse.status(HttpStatus.OK)
                .body(
                        BodyInserters.fromPublisher(
                                serverRequest.bodyToMono(Todo.class).flatMap(todoRepository::save),Todo.class)
                );
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest){
        return ServerResponse.noContent()
                .build(todoRepository.deleteById(serverRequest.pathVariable("id"))
                );
    }
}
