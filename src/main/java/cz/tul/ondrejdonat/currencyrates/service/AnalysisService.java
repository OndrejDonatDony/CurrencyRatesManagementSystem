package cz.tul.ondrejdonat.currencyrates.service;

import cz.tul.ondrejdonat.currencyrates.exception.ApiException;
import cz.tul.ondrejdonat.currencyrates.model.dto.AnalysisResultDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.AverageRatesDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
import cz.tul.ondrejdonat.currencyrates.model.dto.TimeseriesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AnalysisService {

    private final ExchangeRateService exchangeRateService;

    public AnalysisService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public AnalysisResultDto analyzeLatestRates(String base, String symbols) {
        log.info("Spoustim analyzu aktualnich kurzu. base={}, symbols={}", base, symbols);

        ExchangeRateResponse response = exchangeRateService.getLatestRates(base, symbols);

        if (response.getRates() == null || response.getRates().isEmpty()) {
            log.error("API nevratilo zadna data pro analyzu.");
            throw new ApiException("API nevratilo platna data pro analyzu.");
        }

        Map<String, Double> rates = response.getRates();

        String strongestCurrency = null;
        Double strongestValue = null;
        String weakestCurrency = null;
        Double weakestValue = null;

        for (Map.Entry<String, Double> entry : rates.entrySet()) {
            String currency = entry.getKey();
            Double value = entry.getValue();

            if (strongestValue == null || value > strongestValue) {
                strongestValue = value;
                strongestCurrency = currency;
            }
            if (weakestValue == null || value < weakestValue) {
                weakestValue = value;
                weakestCurrency = currency;
            }
        }

        log.info("Analyza dokoncena. strongest={} weakest={}", strongestCurrency, weakestCurrency);

        return new AnalysisResultDto(
                strongestCurrency,
                strongestValue,
                weakestCurrency,
                weakestValue
        );
    }

    public AverageRatesDto calculateAverageRates(String base, String symbols, String startDate, String endDate) {
        log.info("Spoustim vypocet prumeru kurzu. base={}, symbols={}, startDate={}, endDate={}",
                base, symbols, startDate, endDate);

        TimeseriesResponse response = exchangeRateService.getTimeseries(base, symbols, startDate, endDate);

        if (response.getRates() == null || response.getRates().isEmpty()) {
            log.error("API nevratilo zadna historicka data pro vypocet prumeru.");
            throw new ApiException("API nevratilo platna historicka data.");
        }

        Map<String, Double> sums = new HashMap<>();

        Map<String, Integer> counts = new HashMap<>();

        for (Map<String, Double> dailyRates : response.getRates().values()) {
            for (Map.Entry<String, Double> entry : dailyRates.entrySet()) {
                String currency = entry.getKey();
                Double rate = entry.getValue();

                if (rate == null) {
                    continue;
                }
                sums.put(currency, sums.getOrDefault(currency, 0.0) + rate);
                counts.put(currency, counts.getOrDefault(currency, 0) + 1);
            }
        }

        Map<String, Double> averages = new HashMap<>();

        for (String currency : sums.keySet()) {
            averages.put(currency, sums.get(currency) / counts.get(currency));
        }

        log.info("Vypocet prumeru dokoncen. currencies={}", averages.keySet());

        return new AverageRatesDto(base, startDate, endDate, averages);
    }
}