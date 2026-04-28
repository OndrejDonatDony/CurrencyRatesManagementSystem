package cz.tul.ondrejdonat.currencyrates.exception;

public class SettingsNotFoundException extends RuntimeException {

    public SettingsNotFoundException(String message) {
        super(message);
    }
}