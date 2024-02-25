package com.bsolz.orderservice.services;

import com.bsolz.orderservice.clients.ProductClient;
import com.bsolz.orderservice.clients.UserClient;
import com.bsolz.orderservice.dtos.OrderRequestDto;
import com.bsolz.orderservice.dtos.OrderResponseDto;
import com.bsolz.orderservice.dtos.RequestContext;
import com.bsolz.orderservice.entities.Order;
import com.bsolz.orderservice.repositories.OrderRepository;
import com.bsolz.orderservice.utils.EntityDtoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
public class OrderService {

    private final ProductClient productClient;
    private final UserClient userClient;
    private final OrderRepository orderRepository;

    public OrderService(ProductClient productClient, UserClient userClient, OrderRepository orderRepository) {
        this.productClient = productClient;
        this.userClient = userClient;
        this.orderRepository = orderRepository;
    }

    public Mono<OrderResponseDto> processOrder(Mono<OrderRequestDto> orderRequestDtoMono) {
        return orderRequestDtoMono.map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                .map(EntityDtoUtil::getOrder)
                .map(this.orderRepository::save)
                .map(EntityDtoUtil::getOrderResponse)
                .subscribeOn(Schedulers.boundedElastic()); // For blocking operation , event loop thread will not  affected
    }

    public Flux<OrderResponseDto> getOrderByUserId(int userId) {
        return Flux.fromStream(() -> this.orderRepository.findByUserId(userId).stream())
                .map(EntityDtoUtil::getOrderResponse)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<RequestContext> productRequestResponse(RequestContext rc) {
        return this.productClient.getProductById(rc.getOrderRequestDto().getProductId())
                .doOnNext(rc::setProductDto)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .thenReturn(rc);
    }

    private Mono<RequestContext> userRequestResponse(RequestContext rc) {
        return this.userClient.getTransaction(rc.getTransactionRequestDto())
                .doOnNext(rc::setTransactionResponseDto)
                .thenReturn(rc);
    }
}
