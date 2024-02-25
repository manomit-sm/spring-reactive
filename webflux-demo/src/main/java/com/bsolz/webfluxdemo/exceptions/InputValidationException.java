package com.bsolz.webfluxdemo.exceptions;

public class InputValidationException extends RuntimeException {

    private static final String MSG = " Allowed range is 10 - 50";
    private static final int ERROR_CODE = 100;

    private final int input;

    public InputValidationException(int input) {
        super(MSG);
        this.input = input;
    }

    public int getInput() {
        return input;
    }
    public int getErrorCode() {
        return ERROR_CODE;
    }
}
