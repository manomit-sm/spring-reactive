package com.bsolz.webfluxdemo.config;

import com.bsolz.webfluxdemo.dtos.InputFailedValidationResponse;
import com.bsolz.webfluxdemo.dtos.MultiplyRequest;
import com.bsolz.webfluxdemo.dtos.Response;
import com.bsolz.webfluxdemo.exceptions.InputValidationException;
import com.bsolz.webfluxdemo.services.MathService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RequestHandler {

    private final MathService mathService;

    public RequestHandler(MathService mathService) {
        this.mathService = mathService;
    }

    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
        var input = Integer.parseInt(serverRequest.pathVariable("input"));
        return ServerResponse.ok().body(this.mathService.findSquare(input), Response.class);

    }

    public Mono<ServerResponse> tableHandler(ServerRequest serverRequest) {
        var input = Integer.parseInt(serverRequest.pathVariable("input"));
        return ServerResponse.ok().body(this.mathService.multiplicationTable(input), Response.class);

    }

    public Mono<ServerResponse> tableStreamHandler(ServerRequest serverRequest) {
        var input = Integer.parseInt(serverRequest.pathVariable("input"));
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(this.mathService.multiplicationTable(input), Response.class);

    }

    public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
        var body = serverRequest.bodyToMono(MultiplyRequest.class);
        var responseMono = mathService.multiply(body);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseMono, Response.class);

    }

    public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest serverRequest) {
        var input = Integer.parseInt(serverRequest.pathVariable("input"));
        if (input > 50)
            Mono.error(new InputValidationException(input));
        return ServerResponse.ok().body(this.mathService.findSquare(input), Response.class);

    }
}
