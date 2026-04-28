package cz.tul.ondrejdonat.currencyrates.client;

import cz.tul.ondrejdonat.currencyrates.exception.ApiException;
import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
import cz.tul.ondrejdonat.currencyrates.model.dto.TimeseriesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
public class ExchangeRateApiClient {

    private final RestTemplate restTemplate;

    @Value("${exchange.api.key}")
    private String apiKey;

    public ExchangeRateApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ExchangeRateResponse getLatestRates(String base, String symbols) {
        String url = "https://api.apilayer.com/exchangerates_data/latest"
                + "?base=" + base
                + "&symbols=" + symbols;

        log.info("Volam API latest. base={}, symbols={}", base, symbols);

        try {
            ResponseEntity<ExchangeRateResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHttpEntity(),
                    ExchangeRateResponse.class
            );

            validateLatestResponse(response.getBody());
            return response.getBody();

        } catch (RestClientException e) {
            log.error("Chyba pri volani API latest.", e);
            throw new ApiException("Nepodarilo se nacist aktualni kurzy z API.");
        }
    }

    public TimeseriesResponse getTimeseries(String base, String symbols, String startDate, String endDate) {
        String url = "https://api.apilayer.com/exchangerates_data/timeseries"
                + "?base=" + base
                + "&symbols=" + symbols
                + "&start_date=" + startDate
                + "&end_date=" + endDate;

        log.info("Volam API timeseries. base={}, symbols={}, startDate={}, endDate={}",
                base, symbols, startDate, endDate);

        try {
            ResponseEntity<TimeseriesResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHttpEntity(),
                    TimeseriesResponse.class
            );

            validateTimeseriesResponse(response.getBody());
            return response.getBody();

        } catch (RestClientException e) {
            log.error("Chyba pri volani API timeseries.", e);
            throw new ApiException("Nepodarilo se nacist historicka data z API.");
        }
    }

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);
        return new HttpEntity<>(headers);
    }

    private void validateLatestResponse(ExchangeRateResponse response) {
        if (response == null) {
            throw new ApiException("API vratilo prazdnou odpoved.");
        }

        if (!response.isSuccess()) {
            String message = response.getError() != null
                    ? response.getError().getInfo()
                    : "API nevratilo uspesnou odpoved.";

            throw new ApiException(message);
        }
    }

    private void validateTimeseriesResponse(TimeseriesResponse response) {
        if (response == null) {
            throw new ApiException("API vratilo prazdnou odpoved.");
        }

        if (!response.isSuccess()) {
            String message = response.getError() != null
                    ? response.getError().getInfo()
                    : "API nevratilo uspesnou odpoved.";

            throw new ApiException(message);
        }
    }
}