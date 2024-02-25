package com.bsolz.userservice.services;

import com.bsolz.userservice.dtos.TransactionRequestDto;
import com.bsolz.userservice.dtos.TransactionResponse;
import com.bsolz.userservice.dtos.TransactionStatus;
import com.bsolz.userservice.repositories.UserRepository;
import com.bsolz.userservice.repositories.UserTransactionRepository;
import com.bsolz.userservice.utils.EntityDtoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserTransactionService {

    private final UserRepository userRepository;
    private final UserTransactionRepository transactionRepository;

    public UserTransactionService(UserRepository userRepository, UserTransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public Mono<TransactionResponse> createTransaction(final TransactionRequestDto requestDto) {
        return userRepository.updateUserBalance(requestDto.getUserId(), requestDto.getAmount())
                .filter(Boolean::booleanValue)
                .map(b -> EntityDtoUtil.toEntity(requestDto))
                .flatMap(transactionRepository::save)
                .map(ut -> EntityDtoUtil.toDto(requestDto, TransactionStatus.APPROVED))
                .defaultIfEmpty(EntityDtoUtil.toDto(requestDto, TransactionStatus.DECLINED));
    }
}
