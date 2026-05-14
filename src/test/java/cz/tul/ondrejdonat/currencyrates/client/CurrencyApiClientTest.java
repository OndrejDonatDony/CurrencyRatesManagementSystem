package cz.tul.ondrejdonat.currencyrates.client;
import cz.tul.ondrejdonat.currencyrates.exception.ApiException;

import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
import cz.tul.ondrejdonat.currencyrates.model.dto.TimeseriesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyApiClientTest {
    @Mock
    private RestTemplate restTemplate;

    @Test
    public void testGetLatestRates() {
        ExchangeRateApiClient client =
                new ExchangeRateApiClient(restTemplate, "test-key");

        ExchangeRateResponse response = new ExchangeRateResponse(
                true, 123L, "EUR", "2026-04-28",
                Map.of("USD", 1.1),
                null
        );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(ExchangeRateResponse.class)
        )).thenReturn(ResponseEntity.ok(response));

        ExchangeRateResponse result =
                client.getLatestRates("EUR", "USD");

        assertEquals("EUR", result.getBase());
        assertEquals(1.1, result.getRates().get("USD"));
    }

    @Test
    public void testGetTimeseries() {
        ExchangeRateApiClient client =
                new ExchangeRateApiClient(restTemplate, "test-key");

        TimeseriesResponse response = new TimeseriesResponse(
                true,
                true,
                "EUR",
                "2026-04-27",
                "2026-04-28",
                Map.of("2026-04-27", Map.of("USD", 1.1)),
                null
        );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(TimeseriesResponse.class)
        )).thenReturn(ResponseEntity.ok(response));

        TimeseriesResponse result =
                client.getTimeseries("EUR", "USD", "2026-04-27", "2026-04-28");

        assertEquals("EUR", result.getBase());
        assertTrue(result.isTimeseries());
    }

    @Test
    public void testApiFailure() {
        ExchangeRateApiClient client =
                new ExchangeRateApiClient(restTemplate, "test-key");

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(ExchangeRateResponse.class)
        )).thenThrow(new RestClientException("chyba"));

        assertThrows(ApiException.class, () ->
                client.getLatestRates("EUR", "USD")
        );
    }

    @Test
    public void testEmptyResponse() {
        ExchangeRateApiClient client =
                new ExchangeRateApiClient(restTemplate, "test-key");

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(ExchangeRateResponse.class)
        )).thenReturn(ResponseEntity.ok(null));

        assertThrows(ApiException.class, () ->
                client.getLatestRates("EUR", "USD")
        );
    }

    @Test
    public void testLatestNotSuccessful() {
        ExchangeRateApiClient client =
                new ExchangeRateApiClient(restTemplate, "test-key");

        ExchangeRateResponse response = new ExchangeRateResponse(
                false, 123L, "EUR", "2026-04-28",
                null,
                null
        );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(ExchangeRateResponse.class)
        )).thenReturn(ResponseEntity.ok(response));

        assertThrows(ApiException.class, () ->
                client.getLatestRates("EUR", "USD")
        );
    }

    @Test
    public void testTimeseriesEmptyResponse() {
        ExchangeRateApiClient client =
                new ExchangeRateApiClient(restTemplate, "test-key");

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(TimeseriesResponse.class)
        )).thenReturn(ResponseEntity.ok(null));

        assertThrows(ApiException.class, () ->
                client.getTimeseries("EUR", "USD", "2026-04-27", "2026-04-28")
        );
    }

    @Test
    public void testTimeseriesNotSuccessful() {
        ExchangeRateApiClient client =
                new ExchangeRateApiClient(restTemplate, "test-key");

        TimeseriesResponse response = new TimeseriesResponse(
                false,
                true,
                "EUR",
                "2026-04-27",
                "2026-04-28",
                null,
                null
        );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(TimeseriesResponse.class)
        )).thenReturn(ResponseEntity.ok(response));

        assertThrows(ApiException.class, () ->
                client.getTimeseries("EUR", "USD", "2026-04-27", "2026-04-28")
        );
    }

    @Test
    public void testTimeseriesApiFailure() {
        ExchangeRateApiClient client =
                new ExchangeRateApiClient(restTemplate, "test-key");

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(TimeseriesResponse.class)
        )).thenThrow(new RestClientException("chyba"));

        assertThrows(ApiException.class, () ->
                client.getTimeseries("EUR", "USD", "2026-04-27", "2026-04-28")
        );
    }
}