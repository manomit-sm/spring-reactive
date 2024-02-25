package com.bsolz.webfluxdemo.webtestclient;

import com.bsolz.webfluxdemo.config.RequestHandler;
import com.bsolz.webfluxdemo.config.RouterConfig;
import com.bsolz.webfluxdemo.dtos.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Date;

@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {RouterConfig.class})
public class RouterFunctionTest {

    private WebTestClient client;

    //@Autowired
    //private RouterConfig routerConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private RequestHandler requestHandler;

    @BeforeAll
    public void setClient() {
        this.client = WebTestClient
                .bindToApplicationContext(applicationContext).build();

        // WebTestClient.bindToServer().baseUrl("remote-server-url").build();
    }

    @Test
    public void routerFunctionTest() {
        Mockito.when(requestHandler.squareHandler(Mockito.any()))
                .thenReturn(ServerResponse.ok().bodyValue(new Response(new Date(), 25)));

        this.client
                .get()
                .uri("/router/square/{input}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Response.class)
                .value(c -> Assertions.assertThat(c.output()).isEqualTo(25));
    }
}
