package cz.tul.ondrejdonat.currencyrates.model.dto;

public class AnalysisResultDto {
    private String strongestCurrency;
    private Double strongestValue;
    private String weakestCurrency;
    private Double weakestValue;

    public AnalysisResultDto() {
    }

    public AnalysisResultDto(String strongestCurrency, Double strongestValue, String weakestCurrency, Double weakestValue) {
        this.strongestCurrency = strongestCurrency;
        this.strongestValue = strongestValue;
        this.weakestCurrency = weakestCurrency;
        this.weakestValue = weakestValue;
    }

    public String getStrongestCurrency() {
        return strongestCurrency;
    }

    public void setStrongestCurrency(String strongestCurrency) {
        this.strongestCurrency = strongestCurrency;
    }

    public Double getStrongestValue() {
        return strongestValue;
    }

    public void setStrongestValue(Double strongestValue) {
        this.strongestValue = strongestValue;
    }

    public String getWeakestCurrency() {
        return weakestCurrency;
    }

    public void setWeakestCurrency(String weakestCurrency) {
        this.weakestCurrency = weakestCurrency;
    }

    public Double getWeakestValue() {
        return weakestValue;
    }

    public void setWeakestValue(Double weakestValue) {
        this.weakestValue = weakestValue;
    }
}