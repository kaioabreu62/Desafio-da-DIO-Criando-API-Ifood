package me.dio.innovation.one.service.exception;

public class NotFoundException extends BusinessException {
    public NotFoundException() {
        super("Resource not found.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
