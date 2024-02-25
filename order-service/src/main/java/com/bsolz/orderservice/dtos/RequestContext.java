package com.bsolz.orderservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestContext {
    private OrderRequestDto orderRequestDto;
    private ProductDto productDto;
    private TransactionRequestDto transactionRequestDto;
    private TransactionResponseDto transactionResponseDto;

    public RequestContext(OrderRequestDto orderRequestDto) {
        this.orderRequestDto = orderRequestDto;
    }
}
