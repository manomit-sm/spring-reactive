package com.bsolz.webfluxdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .filter(this::sessionTokenGenerator)
                //.defaultHeaders(h -> h.setBasicAuth("username", "password"))
                .build();
    }

    /* private Mono<ClientResponse> sessionTokenGenerator(ClientRequest request, ExchangeFunction ex) {
        var clientRequest = ClientRequest.from(request)
                .headers(h -> h.setBearerAuth("some-lengthy-jwt")).build();
        return ex.exchange(clientRequest);
    } */

    private Mono<ClientResponse> sessionTokenGenerator(ClientRequest request, ExchangeFunction ex) {
        var clientRequest = request.attribute("auth")
                .map(v -> v.equals("basic") ? withBasicAuth(request) : withBearerAuth(request))
                .orElse(request);
        return ex.exchange(clientRequest);
    }

    private ClientRequest withBasicAuth(ClientRequest request) {
        return ClientRequest.from(request)
                .headers(h -> h.setBasicAuth("username", "password"))
                .build();
    }
    private ClientRequest withBearerAuth(ClientRequest request) {
        return ClientRequest.from(request)
                .headers(h -> h.setBearerAuth("some-token"))
                .build();
    }
}
