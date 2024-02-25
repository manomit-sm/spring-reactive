package com.bsolz.webfluxdemo.dtos;

public record InputFailedValidationResponse(int errorCode, int input, String message) {
}
