package com.bsolz.userservice.dtos;

import lombok.Data;

@Data
public class UserDto {

    private Integer id;
    private String name;
    private Integer balance;
}
