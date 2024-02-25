package com.bsolz.webfluxdemo.webtestclient;

import com.bsolz.webfluxdemo.controllers.MathController;
import com.bsolz.webfluxdemo.dtos.MultiplyRequest;
import com.bsolz.webfluxdemo.dtos.Response;
import com.bsolz.webfluxdemo.services.MathService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

@WebFluxTest(MathController.class)
public class ControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private MathService mathService;

    @Test
    public void singleResponseTest() {
        Mockito.when(mathService.findSquare(Mockito.anyInt())).thenReturn(Mono.just(new Response(new Date(), 25)));

        this.client
                .get()
                .uri("/math/square/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value(c -> Assertions.assertThat(c.output()).isEqualTo(25));


    }

    @Test
    public void listResponseTest() {
        var flux = Flux.range(1, 3)
                        .map(r -> new Response(new Date(), r));
        Mockito.when(mathService.multiplicationTable(Mockito.anyInt())).thenReturn(flux);

        this.client
                .get()
                .uri("/math/table/{input}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Response.class)
                .hasSize(3);



    }

    @Test
    public void streamingResponseTest() {
        var flux = Flux.range(1, 3)
                .map(r -> new Response(new Date(), r))
                .delayElements(Duration.ofMillis(100));
        Mockito.when(mathService.multiplicationTable(Mockito.anyInt())).thenReturn(flux);

        this.client
                .get()
                .uri("/math/table/{input}/stream", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(Response.class)
                .hasSize(3);



    }

    @Test
    public void paramResponseTest() {
        var map = Map.of(
            "count", 10,
            "page", 20
        );
        this.client
                .get()
                .uri(b -> b.path("/jobs/search").query("count={count}&page={page}").build(map))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(2).contains(10, 20);
    }

    @Test
    public void postMultiplyTest() {
        Mockito.when(mathService.multiply(Mockito.any()))
                .thenReturn(Mono.just(new Response(new Date(), 200)));


        this.client
                .post()
                .uri("/math/multiply")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBasicAuth("admin", "admin"))
                .headers(h -> h.set("key", "value"))
                .bodyValue(new MultiplyRequest(10, 20))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
