package cz.tul.ondrejdonat.currencyrates.client;

import cz.tul.ondrejdonat.currencyrates.exception.ApiException;
import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
import cz.tul.ondrejdonat.currencyrates.model.dto.TimeseriesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ExchangeRateApiClient {

    private final RestTemplate restTemplate;
    private final String apiKey;

    public ExchangeRateApiClient(RestTemplate restTemplate,
                                 @Value("${exchange.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }
    public ExchangeRateResponse getLatestRates(String base, String symbols) {
        String url = "https://api.apilayer.com/exchangerates_data/latest"
                + "?base=" + base
                + "&symbols=" + symbols;
        try {
            ResponseEntity<ExchangeRateResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestWithApiKey(),
                    ExchangeRateResponse.class
            );
            ExchangeRateResponse body = response.getBody();

            if (body == null) {
                throw new ApiException("API nevratilo zadna data.");
            }


            if (!body.isSuccess()) {
                throw new ApiException(getApiError(body.getError()));
            }
            return body;
        } catch (RestClientException e) {
            log.error("Nepodarilo se nacist aktualni kurzy", e);
            throw new ApiException("Nepodarilo se nacist aktualni kurzy.");
        }
    }
    public TimeseriesResponse getTimeseries(String base, String symbols, String startDate, String endDate) {
        String url = "https://api.apilayer.com/exchangerates_data/timeseries"
                + "?base=" + base
                + "&symbols=" + symbols
                + "&start_date=" + startDate
                + "&end_date=" + endDate;
        try {
            ResponseEntity<TimeseriesResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestWithApiKey(),
                    TimeseriesResponse.class
            );

            TimeseriesResponse body = response.getBody();

            if (body == null) {
                throw new ApiException("API nevratilo zadna data.");
            }
            if (!body.isSuccess()) {
                throw new ApiException(getApiError(body.getError()));
            }

            return body;

        } catch (RestClientException e) {
            log.error("Nepodarilo se nacist historicka data", e);
            throw new ApiException("Nepodarilo se nacist historicka data.");
        }
    }

    private HttpEntity<String> requestWithApiKey() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);
        return new HttpEntity<>(headers);
    }


    private String getApiError(ExchangeRateResponse.ApiError error) {
        if (error == null || error.getInfo() == null) {
            return "API vratilo chybu.";
        }

        return error.getInfo();
    }
}