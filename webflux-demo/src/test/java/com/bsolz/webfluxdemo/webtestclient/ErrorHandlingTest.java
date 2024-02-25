package com.bsolz.webfluxdemo.webtestclient;

import com.bsolz.webfluxdemo.controllers.MathController;
import com.bsolz.webfluxdemo.dtos.MultiplyRequest;
import com.bsolz.webfluxdemo.dtos.Response;
import com.bsolz.webfluxdemo.services.MathService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Date;

@WebFluxTest(MathController.class)
public class ErrorHandlingTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private MathService mathService;
    @Test
    public void errorHandlingTest() {
        Mockito.when(mathService.findSquare(Mockito.anyInt()))
                .thenReturn(Mono.just(new Response(new Date(), 1)));


        this.client
                .get()
                .uri("/math/square/{input}", 52)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Some error message");

    }
}
