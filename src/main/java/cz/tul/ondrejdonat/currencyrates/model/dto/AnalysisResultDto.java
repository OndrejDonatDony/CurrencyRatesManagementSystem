package cz.tul.ondrejdonat.currencyrates.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResultDto {

    private String strongestCurrency;
    private Double strongestValue;
    private String weakestCurrency;
    private Double weakestValue;
}