package com.bsolz.userservice.controllers;

import com.bsolz.userservice.UserServiceApplication;
import com.bsolz.userservice.dtos.TransactionRequestDto;
import com.bsolz.userservice.dtos.TransactionResponse;
import com.bsolz.userservice.services.UserTransactionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user/transaction")
public class UserTransactionController {

    private final UserTransactionService userTransactionService;

    public UserTransactionController(UserTransactionService userTransactionService) {
        this.userTransactionService = userTransactionService;
    }

    public Mono<TransactionResponse> createTransaction(@RequestBody Mono<TransactionRequestDto> requestDtoMono) {
        return requestDtoMono.flatMap(userTransactionService::createTransaction);
    }
}
