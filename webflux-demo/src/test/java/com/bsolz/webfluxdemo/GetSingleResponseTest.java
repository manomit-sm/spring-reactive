package com.bsolz.webfluxdemo;

import com.bsolz.webfluxdemo.dtos.MultiplyRequest;
import com.bsolz.webfluxdemo.dtos.Response;
import com.bsolz.webfluxdemo.exceptions.InputValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

public class GetSingleResponseTest extends BaseTests {

    @Autowired
    public WebClient webClient;

    private static final String QUERY_STRING = "http://localhost:8080/jobs/search?count={count}&page={page}";

    @Test
    public void blockTest() {
        var response = webClient
                .get()
                .uri("/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .block();
    }

    @Test
    public void stepVerifierTest() {
        var responseMono = webClient
                .get()
                .uri("/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class);

        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r.output() == 25)
                .verifyComplete();
    }

    @Test
    public void fluxResponseTest() {
        var responseFlux = webClient
                .get()
                .uri("/table/{input}", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);
        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void fluxStreamResponseTest() {
        var responseFlux = webClient
                .get()
                .uri("/table/{input}/stream", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);
        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void postTest() {
        var responseMono = webClient
                .post()
                .uri("/multiply")
                .bodyValue(buildDto(5, 2))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);
        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .expectNextMatches(r -> r.output() == 10)
                .verifyComplete();
    }

    @Test
    public void headerTest() {
        var responseMono = webClient
                .post()
                .uri("/multiply")
                .attribute("auth", "basic")
                // .headers(h -> h.set("testKey" , "testValue"))
                //.headers(h -> h.setBasicAuth("username" , "password"))
                .bodyValue(buildDto(5, 2))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);
        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .expectNextMatches(r -> r.output() == 10)
                .verifyComplete();
    }

    @Test
    public void badRequestTest() {
        var responseMono = webClient
                .get()
                .uri("/square/{input}/throw", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .doOnError(System.out::println);

        StepVerifier.create(responseMono)
                .verifyError(WebClientResponseException.BadRequest.class);
    }

    @Test
    public void badRequestExchangeTest() {
        var responseMono = webClient
                .get()
                .uri("/square/{input}/throw", 5)
                .exchangeToMono(this::exchange)
                .doOnNext(System.out::println)
                .doOnError(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void queryParamTest() {
        var uri = UriComponentsBuilder
                .fromUriString(QUERY_STRING)
                .build(10, 20);

        Flux<Integer> integerFlux = webClient
                .get()
                // .uri(uri)
                .uri(u -> u.path("jobs/search").query("count={count}&page={page}").build(Map.of("count", 10, "page", 20)))
                .retrieve()
                .bodyToFlux(Integer.class)
                .doOnNext(System.out::println);

        StepVerifier
                .create(integerFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    private MultiplyRequest buildDto(int a, int b) {
        return new MultiplyRequest(a, b);
    }

    private Mono<Object> exchange(ClientResponse cr) {
        if (cr.statusCode() == HttpStatus.BAD_REQUEST)
            return cr.bodyToMono(InputValidationException.class);
        else
            return cr.bodyToMono(Response.class);
    }
}
