package cz.tul.ondrejdonat.currencyrates.exception;

import cz.tul.ondrejdonat.currencyrates.service.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final LogService logService;

    public GlobalExceptionHandler(LogService logService) {
        this.logService = logService;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiException(ApiException e) {

        logService.log("ERROR", e.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(SettingsNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSettingsException(SettingsNotFoundException e) {

        logService.log("ERROR", e.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {

        logService.log("ERROR", e.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("error", "Neocekavana chyba.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}