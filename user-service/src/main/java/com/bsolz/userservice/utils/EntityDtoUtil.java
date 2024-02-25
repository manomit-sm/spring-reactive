package com.bsolz.userservice.utils;

import com.bsolz.userservice.dtos.TransactionRequestDto;
import com.bsolz.userservice.dtos.TransactionResponse;
import com.bsolz.userservice.dtos.TransactionStatus;
import com.bsolz.userservice.dtos.UserDto;
import com.bsolz.userservice.entities.User;
import com.bsolz.userservice.entities.UserTransaction;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class EntityDtoUtil {
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }

    public static UserTransaction toEntity(TransactionRequestDto requestDto) {
        UserTransaction ut = new UserTransaction();
        ut.setUserId(requestDto.getUserId());
        ut.setAmount(requestDto.getAmount());
        ut.setTransactionDate(LocalDateTime.now());
        return ut;
    }

    public static TransactionResponse toDto(TransactionRequestDto requestDto, TransactionStatus status) {
        TransactionResponse response = new TransactionResponse();
        response.setAmount(requestDto.getAmount());
        response.setUserId(requestDto.getUserId());
        response.setStatus(status);
        return response;
    }
}
