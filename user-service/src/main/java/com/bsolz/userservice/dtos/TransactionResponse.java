package com.bsolz.userservice.dtos;

import lombok.Data;

@Data
public class TransactionResponse {
    private Integer userId;
    private Integer amount;

    private TransactionStatus status;
}
