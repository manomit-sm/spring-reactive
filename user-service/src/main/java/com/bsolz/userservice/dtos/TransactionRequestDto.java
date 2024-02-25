package com.bsolz.userservice.dtos;

import lombok.Data;

@Data
public class TransactionRequestDto {
    private Integer userId;
    private Integer amount;
}
