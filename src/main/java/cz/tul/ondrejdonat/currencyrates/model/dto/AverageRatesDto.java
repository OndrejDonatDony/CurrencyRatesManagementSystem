package cz.tul.ondrejdonat.currencyrates.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AverageRatesDto {

    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Double> averageRates;
}