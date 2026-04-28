package cz.tul.ondrejdonat.currencyrates.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {

    private boolean success;
    private Long timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;
    private ApiError error;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiError {
        private Integer code;
        private String type;
        private String info;
    }
}