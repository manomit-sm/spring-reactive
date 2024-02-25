package com.bsolz.webfluxdemo.config;

import com.bsolz.webfluxdemo.dtos.InputFailedValidationResponse;
import com.bsolz.webfluxdemo.exceptions.InputValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Configuration
public class RouterConfig {

    private final RequestHandler requestHandler;

    public RouterConfig(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> highLevelRouterFunction() {
        return RouterFunctions.route()
                .path("router", this::responseRouterFunction)
                .build();
    }

    private RouterFunction<ServerResponse> responseRouterFunction() {
        return RouterFunctions.route()
                .GET("square/{input}", RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")), requestHandler::squareHandler)
                .GET("square/{input}", req -> ServerResponse.badRequest().bodyValue("Only 10 to 19 allowed"))
                .GET("table/{input}", requestHandler::tableHandler)
                .GET("table/{input}/stream", requestHandler::tableStreamHandler)
                .POST("multiply", requestHandler::multiplyHandler)
                .GET("table/{input}/validation", requestHandler::squareHandlerWithValidation)
                .onError(InputValidationException.class, exceptionHandler())
                .build();
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        return (err, req) -> {
          var ex = (InputValidationException) err;
          return ServerResponse.badRequest()
                  .bodyValue(
                          new InputFailedValidationResponse(
                                  ex.getInput(),
                                  ex.getErrorCode(),
                                  ex.getMessage()
                          )
                  );
        };
    }
}
