package com.bsolz.webfluxdemo.controllers;

import com.bsolz.webfluxdemo.dtos.MultiplyRequest;
import com.bsolz.webfluxdemo.dtos.Response;
import com.bsolz.webfluxdemo.exceptions.InputValidationException;
import com.bsolz.webfluxdemo.services.MathService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("math")
public class MathController {

    private final MathService mathService;

    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("square/{input}")
    public Mono<Response> findSquare(@PathVariable int input) {
        return Mono.just(input)
                .handle((integer, sink) -> {
                    if (integer > 50)
                        sink.error(new InputValidationException(integer));
                    else
                        sink.next(integer);
                }).cast(Integer.class)
                .flatMap(this.mathService::findSquare);
        // return this.mathService.findSquare(input);
    }

    @GetMapping("table/{input}")
    public Flux<Response> multiplicationTable(@PathVariable int input) {
        return this.mathService.multiplicationTable(input);
    }

    @GetMapping(value = "table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> multiplicationTableStream(@PathVariable int input) {

        if (input > 50) {
            throw new InputValidationException(input);
        }

        return this.mathService.multiplicationTable(input);
    }

    @PostMapping("multiply")
    public Mono<Response> multiply(
            @RequestBody Mono<MultiplyRequest> dto,
            @RequestHeader Map<String, String> headers
            ) {
        System.out.println(headers);
        return this.mathService.multiply(dto);
    }

    @GetMapping("square/{input}/nother")
    public Mono<ResponseEntity<Response>> findSquareNew(@PathVariable int input) {
        return Mono.just(input)
                .filter(i -> i <= 50)
                .flatMap(this.mathService::findSquare)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
