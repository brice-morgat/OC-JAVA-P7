package com.nnk.springboot.exceptions;

public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String t) {
        super(t);
    }
}
