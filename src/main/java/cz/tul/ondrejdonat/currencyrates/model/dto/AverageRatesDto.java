package cz.tul.ondrejdonat.currencyrates.model.dto;

import java.util.Map;

public class AverageRatesDto {

    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Double> averageRates;

    public AverageRatesDto() {
    }

    public AverageRatesDto(String base, String startDate, String endDate, Map<String, Double> averageRates) {
        this.base = base;
        this.startDate = startDate;
        this.endDate = endDate;
        this.averageRates = averageRates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<String, Double> getAverageRates() {
        return averageRates;
    }

    public void setAverageRates(Map<String, Double> averageRates) {
        this.averageRates = averageRates;
    }
}