package cz.tul.ondrejdonat.currencyrates.service;

import cz.tul.ondrejdonat.currencyrates.exception.ApiException;
import cz.tul.ondrejdonat.currencyrates.model.dto.AnalysisResultDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.AverageRatesDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
import cz.tul.ondrejdonat.currencyrates.model.dto.TimeseriesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private AnalysisService analysisService;

    @Test
    void analyzeLatestRatesShouldReturnStrongestAndWeakestCurrency() {
        Map<String, Double> rates = new LinkedHashMap<>();
        rates.put("USD", 1.1);
        rates.put("CZK", 24.5);
        rates.put("GBP", 0.85);

        ExchangeRateResponse response = new ExchangeRateResponse(
                true,
                123L,
                "EUR",
                "2026-04-28",
                rates,
                null
        );

        when(exchangeRateService.getLatestRates("EUR", "USD,CZK,GBP"))
                .thenReturn(response);

        AnalysisResultDto result = analysisService.analyzeLatestRates("EUR", "USD,CZK,GBP");

        assertEquals("CZK", result.getStrongestCurrency());
        assertEquals(24.5, result.getStrongestValue());
        assertEquals("GBP", result.getWeakestCurrency());
        assertEquals(0.85, result.getWeakestValue());
    }

    @Test
    void analyzeLatestRatesShouldThrowExceptionWhenRatesAreEmpty() {
        ExchangeRateResponse response = new ExchangeRateResponse(
                true,
                123L,
                "EUR",
                "2026-04-28",
                Map.of(),
                null
        );

        when(exchangeRateService.getLatestRates("EUR", "USD,CZK,GBP"))
                .thenReturn(response);

        assertThrows(ApiException.class, () ->
                analysisService.analyzeLatestRates("EUR", "USD,CZK,GBP")
        );
    }

    @Test
    void calculateAverageRatesShouldReturnAverageValues() {
        Map<String, Map<String, Double>> rates = new LinkedHashMap<>();

        rates.put("2026-04-27", Map.of(
                "USD", 1.0,
                "CZK", 24.0
        ));

        rates.put("2026-04-28", Map.of(
                "USD", 1.2,
                "CZK", 26.0
        ));

        TimeseriesResponse response = new TimeseriesResponse(
                true,
                true,
                "EUR",
                "2026-04-27",
                "2026-04-28",
                rates,
                null
        );

        when(exchangeRateService.getTimeseries(
                "EUR",
                "USD,CZK",
                "2026-04-27",
                "2026-04-28"
        )).thenReturn(response);

        AverageRatesDto result = analysisService.calculateAverageRates(
                "EUR",
                "USD,CZK",
                "2026-04-27",
                "2026-04-28"
        );

        assertEquals("EUR", result.getBase());
        assertEquals(1.1, result.getAverageRates().get("USD"));
        assertEquals(25.0, result.getAverageRates().get("CZK"));
    }

    @Test
    void calculateAverageRatesShouldIgnoreNullValues() {
        Map<String, Map<String, Double>> rates = new LinkedHashMap<>();

        Map<String, Double> day1 = new LinkedHashMap<>();
        day1.put("USD", 1.0);
        day1.put("CZK", null);

        Map<String, Double> day2 = new LinkedHashMap<>();
        day2.put("USD", 1.2);
        day2.put("CZK", 25.0);

        rates.put("2026-04-27", day1);
        rates.put("2026-04-28", day2);

        TimeseriesResponse response = new TimeseriesResponse(
                true,
                true,
                "EUR",
                "2026-04-27",
                "2026-04-28",
                rates,
                null
        );

        when(exchangeRateService.getTimeseries(
                "EUR",
                "USD,CZK",
                "2026-04-27",
                "2026-04-28"
        )).thenReturn(response);

        AverageRatesDto result = analysisService.calculateAverageRates(
                "EUR",
                "USD,CZK",
                "2026-04-27",
                "2026-04-28"
        );

        assertEquals(1.1, result.getAverageRates().get("USD"));
        assertEquals(25.0, result.getAverageRates().get("CZK"));
    }
}