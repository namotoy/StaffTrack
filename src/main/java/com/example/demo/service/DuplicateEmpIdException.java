package com.example.demo.service;

public class DuplicateEmpIdException extends RuntimeException {
    public DuplicateEmpIdException(String message) {
        super(message);
    }
}
