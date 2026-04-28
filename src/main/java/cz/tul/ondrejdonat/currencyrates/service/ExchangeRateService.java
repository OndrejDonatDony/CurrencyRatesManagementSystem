package cz.tul.ondrejdonat.currencyrates.service;

import cz.tul.ondrejdonat.currencyrates.client.ExchangeRateApiClient;
import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
import cz.tul.ondrejdonat.currencyrates.model.dto.TimeseriesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExchangeRateService {

    private final ExchangeRateApiClient exchangeRateApiClient;

    public ExchangeRateService(ExchangeRateApiClient exchangeRateApiClient) {
        this.exchangeRateApiClient = exchangeRateApiClient;
    }

    public ExchangeRateResponse getLatestRates(String base, String symbols) {
        log.info("Service vrstva: ziskani aktualnich kurzu.");
        return exchangeRateApiClient.getLatestRates(base, symbols);
    }

    public TimeseriesResponse getTimeseries(String base, String symbols, String startDate, String endDate) {
        log.info("Service vrstva: ziskani historickych kurzu.");
        return exchangeRateApiClient.getTimeseries(base, symbols, startDate, endDate);
    }
}