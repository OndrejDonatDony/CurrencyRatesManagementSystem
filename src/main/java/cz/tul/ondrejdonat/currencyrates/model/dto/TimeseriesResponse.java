package cz.tul.ondrejdonat.currencyrates.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeseriesResponse {

    private boolean success;
    private boolean timeseries;
    private String base;
    private String start_date;
    private String end_date;
    private Map<String, Map<String, Double>> rates;
    private ExchangeRateResponse.ApiError error;
}