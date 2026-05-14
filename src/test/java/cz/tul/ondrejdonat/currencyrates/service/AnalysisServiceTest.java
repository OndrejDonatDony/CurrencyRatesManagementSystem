package cz.tul.ondrejdonat.currencyrates.service;
import cz.tul.ondrejdonat.currencyrates.exception.ApiException;
import cz.tul.ondrejdonat.currencyrates.model.dto.AnalysisResultDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.AverageRatesDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
import cz.tul.ondrejdonat.currencyrates.model.dto.TimeseriesResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisServiceTest {

    @Test
    void testAnalyzeLatestRates() {
        ExchangeRateService exchangeRateService = Mockito.mock(ExchangeRateService.class);
        AnalysisService analysisService = new AnalysisService(exchangeRateService);

        Map<String, Double> rates = new LinkedHashMap<>();
        rates.put("USD", 1.1);
        rates.put("CZK", 24.5);
        rates.put("GBP", 0.85);

        ExchangeRateResponse response =
                new ExchangeRateResponse(true, 123L, "EUR", "2026-04-28", rates, null);

        Mockito.when(exchangeRateService.getLatestRates("EUR", "USD,CZK,GBP"))
                .thenReturn(response);

        AnalysisResultDto result =
                analysisService.analyzeLatestRates("EUR", "USD,CZK,GBP");

        assertEquals("CZK", result.getStrongestCurrency());
        assertEquals("GBP", result.getWeakestCurrency());
    }

    @Test
    void testEmptyRates() {
        ExchangeRateService exchangeRateService = Mockito.mock(ExchangeRateService.class);
        AnalysisService analysisService = new AnalysisService(exchangeRateService);

        ExchangeRateResponse response =
                new ExchangeRateResponse(true, 123L, "EUR", "2026-04-28", Map.of(), null);

        Mockito.when(exchangeRateService.getLatestRates("EUR", "USD,CZK,GBP"))
                .thenReturn(response);

        assertThrows(ApiException.class, () ->
                analysisService.analyzeLatestRates("EUR", "USD,CZK,GBP"));
    }

    @Test
    void testAverageRates() {
        ExchangeRateService exchangeRateService = Mockito.mock(ExchangeRateService.class);
        AnalysisService analysisService = new AnalysisService(exchangeRateService);

        Map<String, Map<String, Double>> rates = new LinkedHashMap<>();

        rates.put("2026-04-27", Map.of("USD", 1.0, "CZK", 24.0));
        rates.put("2026-04-28", Map.of("USD", 1.2, "CZK", 26.0));

        TimeseriesResponse response = new TimeseriesResponse(true, true, "EUR",
                "2026-04-27", "2026-04-28", rates, null);


        Mockito.when(exchangeRateService.getTimeseries(
                "EUR", "USD,CZK", "2026-04-27", "2026-04-28"
        )).thenReturn(response);

        AverageRatesDto result = analysisService.calculateAverageRates(
                "EUR", "USD,CZK", "2026-04-27", "2026-04-28");


        assertEquals("EUR", result.getBase());
        assertEquals(1.1, result.getAverageRates().get("USD"));
        assertEquals(25.0, result.getAverageRates().get("CZK"));
    }

    @Test
    void testAnalyzeLatestRatesNullRates() {
        ExchangeRateService exchangeRateService = Mockito.mock(ExchangeRateService.class);
        AnalysisService analysisService = new AnalysisService(exchangeRateService);

        ExchangeRateResponse response =
                new ExchangeRateResponse(true, 123L, "EUR", "2026-04-28", null, null);

        Mockito.when(exchangeRateService.getLatestRates("EUR", "USD,CZK"))
                .thenReturn(response);

        assertThrows(ApiException.class, () ->
                analysisService.analyzeLatestRates("EUR", "USD,CZK"));
    }

    @Test
    void testAverageRatesEmptyRates() {
        ExchangeRateService exchangeRateService = Mockito.mock(ExchangeRateService.class);
        AnalysisService analysisService = new AnalysisService(exchangeRateService);

        TimeseriesResponse response = new TimeseriesResponse(
                true,
                true,
                "EUR",
                "2026-04-27",
                "2026-04-28",
                Map.of(),
                null
        );

        Mockito.when(exchangeRateService.getTimeseries(
                "EUR", "USD,CZK", "2026-04-27", "2026-04-28"
        )).thenReturn(response);

        assertThrows(ApiException.class, () ->
                analysisService.calculateAverageRates(
                        "EUR", "USD,CZK", "2026-04-27", "2026-04-28"));
    }

    @Test
    void testAverageRatesNullRates() {
        ExchangeRateService exchangeRateService = Mockito.mock(ExchangeRateService.class);
        AnalysisService analysisService = new AnalysisService(exchangeRateService);

        TimeseriesResponse response = new TimeseriesResponse(
                true,
                true,
                "EUR",
                "2026-04-27",
                "2026-04-28",
                null,
                null
        );

        Mockito.when(exchangeRateService.getTimeseries(
                "EUR", "USD,CZK", "2026-04-27", "2026-04-28"
        )).thenReturn(response);

        assertThrows(ApiException.class, () ->
                analysisService.calculateAverageRates(
                        "EUR", "USD,CZK", "2026-04-27", "2026-04-28"));
    }

    @Test
    void testAverageRatesIgnoresNullRate() {
        ExchangeRateService exchangeRateService = Mockito.mock(ExchangeRateService.class);
        AnalysisService analysisService = new AnalysisService(exchangeRateService);

        Map<String, Double> firstDay = new LinkedHashMap<>();
        firstDay.put("USD", 1.0);
        firstDay.put("CZK", null);

        Map<String, Double> secondDay = new LinkedHashMap<>();
        secondDay.put("USD", 1.2);
        secondDay.put("CZK", 25.0);

        Map<String, Map<String, Double>> rates = new LinkedHashMap<>();
        rates.put("2026-04-27", firstDay);
        rates.put("2026-04-28", secondDay);

        TimeseriesResponse response = new TimeseriesResponse(
                true,
                true,
                "EUR",
                "2026-04-27",
                "2026-04-28",
                rates,
                null
        );

        Mockito.when(exchangeRateService.getTimeseries(
                "EUR", "USD,CZK", "2026-04-27", "2026-04-28"
        )).thenReturn(response);

        AverageRatesDto result = analysisService.calculateAverageRates(
                "EUR", "USD,CZK", "2026-04-27", "2026-04-28");

        assertEquals(1.1, result.getAverageRates().get("USD"));
        assertEquals(25.0, result.getAverageRates().get("CZK"));
    }

}