package com.bsolz.orderservice.utils;

import com.bsolz.orderservice.dtos.*;
import com.bsolz.orderservice.entities.Order;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static void setTransactionRequestDto(RequestContext requestContext) {
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setUserId(requestContext.getOrderRequestDto().getUserId());
        dto.setAmount(requestContext.getProductDto().getPrice());
        requestContext.setTransactionRequestDto(dto);
    }

    public static Order getOrder(RequestContext requestContext) {
        Order order = new Order();
        order.setUserId(requestContext.getOrderRequestDto().getUserId());
        order.setProductId(requestContext.getOrderRequestDto().getProductId());
        order.setAmount(requestContext.getProductDto().getPrice());

        var status = requestContext.getTransactionResponseDto().getStatus();
        var orderStatus = TransactionStatus.APPROVED.equals(status) ? OrderStatus.COMPLETED : OrderStatus.FAILED;

        order.setStatus(orderStatus);
        return order;
    }

    public static OrderResponseDto getOrderResponse(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        BeanUtils.copyProperties(order, orderResponseDto);
        orderResponseDto.setOrderId(order.getId());
        return orderResponseDto;
    }
}
