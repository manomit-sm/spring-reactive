package com.bsolz.orderservice.controllers;

import com.bsolz.orderservice.dtos.OrderRequestDto;
import com.bsolz.orderservice.dtos.OrderResponseDto;
import com.bsolz.orderservice.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Mono<ResponseEntity<OrderResponseDto>> order(Mono<OrderRequestDto> orderRequestDtoMono) {
        return this.orderService.processOrder(orderRequestDtoMono)
                .map(ResponseEntity::ok)
                .onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
                .onErrorReturn(WebClientRequestException.class, ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

    @GetMapping("user/{userId}")
    public Flux<OrderResponseDto> getOrdersByUser(@PathVariable int userId) {
        return this.orderService.getOrderByUserId(userId);
    }
}
