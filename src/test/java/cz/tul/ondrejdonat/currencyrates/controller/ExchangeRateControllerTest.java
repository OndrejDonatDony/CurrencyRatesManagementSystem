package cz.tul.ondrejdonat.currencyrates.controller;

import cz.tul.ondrejdonat.currencyrates.service.AnalysisService;
import cz.tul.ondrejdonat.currencyrates.service.ExchangeRateService;
import cz.tul.ondrejdonat.currencyrates.service.LogService;
import cz.tul.ondrejdonat.currencyrates.service.SettingsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeRateController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExchangeRateService exchangeRateService;

    @MockitoBean
    private AnalysisService analysisService;

    @MockitoBean
    private LogService logService;

    @MockitoBean
    private SettingsService settingsService;

    @Test
    public void testGetLatestRates() throws Exception {

        mockMvc.perform(get("/api/rates/latest")
                        .param("base", "EUR")
                        .param("symbols", "USD,CZK"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAverageRates() throws Exception {

        mockMvc.perform(get("/api/rates/average")
                        .param("base", "EUR")
                        .param("symbols", "USD,CZK")
                        .param("startDate", "2026-04-27")
                        .param("endDate", "2026-04-28"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAnalysis() throws Exception {

        mockMvc.perform(get("/api/rates/analysis")
                        .param("base", "EUR")
                        .param("symbols", "USD,CZK"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTimeseries() throws Exception {

        mockMvc.perform(get("/api/rates/timeseries")
                        .param("base", "EUR")
                        .param("symbols", "USD,CZK")
                        .param("startDate", "2026-04-27")
                        .param("endDate", "2026-04-28"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLatestRatesError() throws Exception {

        when(exchangeRateService.getLatestRates("EUR", "USD,CZK"))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/rates/latest")
                        .param("base", "EUR")
                        .param("symbols", "USD,CZK"))
                .andExpect(status().is5xxServerError());
    }
}