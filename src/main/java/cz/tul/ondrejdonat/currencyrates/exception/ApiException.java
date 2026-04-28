package cz.tul.ondrejdonat.currencyrates.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}