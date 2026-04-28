package cz.tul.ondrejdonat.currencyrates.exception;

import cz.tul.ondrejdonat.currencyrates.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final LogService logService;

    public GlobalExceptionHandler(LogService logService) {
        this.logService = logService;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiException(ApiException e) {
        log.error("API chyba: {}", e.getMessage(), e);
        logService.log("ERROR", "API chyba: " + e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(SettingsNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSettingsException(SettingsNotFoundException e) {
        log.error("Chyba nastaveni: {}", e.getMessage(), e);
        logService.log("ERROR", "Chyba nastaveni: " + e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        log.error("Necekana chyba aplikace: {}", e.getMessage(), e);
        logService.log("ERROR", "Necekana chyba aplikace: " + e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Doslo k neocekavane chybe aplikace."));
    }
}