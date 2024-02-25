package com.bsolz.productservice.dtos;

public record ProductDto(String id, String description, Integer price) {

    public ProductDto(String description, Integer price) {
        this("", description, price);
    }
}
