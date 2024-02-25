package com.bsolz.orderservice.dtos;

import lombok.Data;

@Data
public class OrderRequestDto {

    private Integer userId;
    private String productId;
}
