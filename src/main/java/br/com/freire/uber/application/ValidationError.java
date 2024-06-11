package br.com.freire.uber.application;

public class ValidationError extends RuntimeException {
    int errorCode;
    public ValidationError(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
