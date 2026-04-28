package cz.tul.ondrejdonat.currencyrates.controller;

import cz.tul.ondrejdonat.currencyrates.model.dto.AnalysisResultDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
import cz.tul.ondrejdonat.currencyrates.model.dto.TimeseriesResponse;
import cz.tul.ondrejdonat.currencyrates.service.AnalysisService;
import cz.tul.ondrejdonat.currencyrates.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cz.tul.ondrejdonat.currencyrates.model.dto.AverageRatesDto;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
    private final AnalysisService analysisService;

    public ExchangeRateController(ExchangeRateService exchangeRateService, AnalysisService analysisService) {
        this.exchangeRateService = exchangeRateService;
        this.analysisService = analysisService;
    }

    @GetMapping("/latest")
    public ExchangeRateResponse getLatestRates(
            @RequestParam String base,
            @RequestParam String symbols) {

        log.info("REST request /api/rates/latest base={}, symbols={}", base, symbols);
        return exchangeRateService.getLatestRates(base, symbols);
    }

    @GetMapping("/timeseries")
    public TimeseriesResponse getTimeseries(
            @RequestParam String base,
            @RequestParam String symbols,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        log.info("REST request /api/rates/timeseries base={}, symbols={}, startDate={}, endDate={}",
                base, symbols, startDate, endDate);

        return exchangeRateService.getTimeseries(base, symbols, startDate, endDate);
    }

    @GetMapping("/analysis")
    public AnalysisResultDto analyzeLatestRates(
            @RequestParam String base,
            @RequestParam String symbols) {

        log.info("REST request /api/rates/analysis base={}, symbols={}", base, symbols);
        return analysisService.analyzeLatestRates(base, symbols);
    }

    @GetMapping("/average")
    public AverageRatesDto getAverageRates(
            @RequestParam String base,
            @RequestParam String symbols,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        log.info("REST request /api/rates/average base={}, symbols={}, startDate={}, endDate={}",
                base, symbols, startDate, endDate);

        return analysisService.calculateAverageRates(base, symbols, startDate, endDate);
    }

}