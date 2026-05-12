package cz.tul.ondrejdonat.currencyrates.exception;

import cz.tul.ondrejdonat.currencyrates.service.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    @Test
    public void testHandleApiException() {

        LogService logService = mock(LogService.class);

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler(logService);

        ApiException exception =
                new ApiException("API chyba");

        ResponseEntity<Map<String, String>> response =
                handler.handleApiException(exception);

        assertEquals(502, response.getStatusCode().value());
        assertEquals("API chyba",
                response.getBody().get("error"));

        verify(logService).log("ERROR", "API chyba");
    }

    @Test
    public void testHandleSettingsException() {

        LogService logService = mock(LogService.class);

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler(logService);

        SettingsNotFoundException exception =
                new SettingsNotFoundException("Nenalezeno");

        ResponseEntity<Map<String, String>> response =
                handler.handleSettingsException(exception);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Nenalezeno",
                response.getBody().get("error"));

        verify(logService).log("ERROR", "Nenalezeno");
    }

    @Test
    public void testHandleException() {

        LogService logService = mock(LogService.class);

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler(logService);

        Exception exception =
                new Exception("Test");

        ResponseEntity<Map<String, String>> response =
                handler.handleException(exception);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Neocekavana chyba.",
                response.getBody().get("error"));

        verify(logService).log("ERROR", "Test");
    }
}