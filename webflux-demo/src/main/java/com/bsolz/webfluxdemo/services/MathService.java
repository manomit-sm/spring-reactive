package com.bsolz.webfluxdemo.services;

import com.bsolz.webfluxdemo.dtos.MultiplyRequest;
import com.bsolz.webfluxdemo.dtos.Response;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MathService {

    public Mono<Response> findSquare(int input) {
        return Mono.fromSupplier(() -> input * input)
                .map(i -> new Response(new Date(), i));
    }

    public Flux<Response> multiplicationTable(int input) {
        /* return IntStream.rangeClosed(1, 10)
                .peek(i -> SleepUtil.sleepSeconds(1))
                .peek(i -> System.out.println("Math Service Processing : " + i))
                .mapToObj(i -> new Response(new Date(), i * input))
                .collect(Collectors.toList()); */
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1)) // Non-Blocking sleep
                //.doOnNext(i -> SleepUtil.sleepSeconds(1)) // this is a blocking sleep
                .doOnNext(i -> System.out.println("Math Service Processing : " + i))
                .map(i -> new Response(new Date(), i * input));
    }

    public Mono<Response> multiply(Mono<MultiplyRequest> dto) {
        return dto
                .map(d -> d.first() * d.second())
                .map(i -> new Response(new Date(), i));
    }
}
