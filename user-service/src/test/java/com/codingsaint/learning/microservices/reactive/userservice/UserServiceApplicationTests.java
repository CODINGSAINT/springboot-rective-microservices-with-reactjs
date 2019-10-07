package com.codingsaint.learning.microservices.reactive.userservice;

import com.codingsaint.learning.microservices.reactive.userservice.model.User;
import com.codingsaint.learning.microservices.reactive.userservice.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT ,
properties = "spring.cloud.discovery.enabled = false")
public class UserServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private static  String id="";

    public void init(){
        id=UUID.randomUUID().toString();
    }

    @Test
    public void addUserTest() {
        init();
        User user= new User();
        user.setId(id);
        user.setName("Namish");
        user.setEmail("namish@codingsaint.com");

       webTestClient.post()
                .uri("/user")
                .body(Mono.just(user), User.class)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Namish");


    }

    @Test
    public void getAllUsers(){
        webTestClient.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(User.class);
    }
    @Test
    public  void  getUser (){
        webTestClient.get().uri("/user/{id}" , Collections.singletonMap("id" ,id))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id);

    }
    @Test
    public  void  deleteUser (){
        webTestClient.delete().uri("/user/{id}" , Collections.singletonMap("id" ,id))
                .exchange()
                .expectStatus().isNoContent();
    }





}
