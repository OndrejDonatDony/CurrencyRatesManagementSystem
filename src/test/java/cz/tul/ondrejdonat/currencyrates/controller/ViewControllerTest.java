package cz.tul.ondrejdonat.currencyrates.controller;

import cz.tul.ondrejdonat.currencyrates.model.dto.AnalysisResultDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
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

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import cz.tul.ondrejdonat.currencyrates.model.dto.AverageRatesDto;
import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;

@WebMvcTest(ViewController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExchangeRateService exchangeRateService;

    @MockitoBean
    private AnalysisService analysisService;

    @MockitoBean
    private SettingsService settingsService;

    @MockitoBean
    private LogService logService;

    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testDashboard() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoadData() throws Exception {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setBase("EUR");
        response.setRates(Map.of("USD", 1.1));

        AnalysisResultDto analysis = new AnalysisResultDto();
        analysis.setStrongestCurrency("USD");
        analysis.setWeakestCurrency("CZK");

        when(exchangeRateService.getLatestRates("EUR", "USD"))
                .thenReturn(response);

        when(analysisService.analyzeLatestRates("EUR", "USD"))
                .thenReturn(analysis);

        mockMvc.perform(post("/dashboard")
                        .param("base", "EUR")
                        .param("symbols", "USD"))
                .andExpect(status().isOk());
    }
    @Test
    public void testLoadDataWithoutBase() throws Exception {

        mockMvc.perform(post("/dashboard")
                        .param("base", "")
                        .param("symbols", "USD"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoadDataWithoutSymbols() throws Exception {

        mockMvc.perform(post("/dashboard")
                        .param("base", "EUR")
                        .param("symbols", ""))
                .andExpect(status().isOk());
    }
    @Test
    public void testDashboardWithSettings() throws Exception {

        when(settingsService.getSettings())
                .thenReturn(new UserSettings(1L, "CZK", "EUR,USD"));

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDashboardWithoutSettings() throws Exception {

        when(settingsService.getSettings())
                .thenReturn(null);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoadDataWithAverage() throws Exception {

        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setBase("EUR");
        response.setRates(Map.of("USD", 1.1));

        AnalysisResultDto analysis = new AnalysisResultDto();
        analysis.setStrongestCurrency("USD");
        analysis.setWeakestCurrency("USD");

        AverageRatesDto average = new AverageRatesDto();
        average.setBase("EUR");
        average.setAverageRates(Map.of("USD", 1.15));

        when(exchangeRateService.getLatestRates("EUR", "USD"))
                .thenReturn(response);

        when(analysisService.analyzeLatestRates("EUR", "USD"))
                .thenReturn(analysis);

        when(analysisService.calculateAverageRates(
                "EUR", "USD", "2026-04-27", "2026-04-28"))
                .thenReturn(average);

        mockMvc.perform(post("/dashboard")
                        .param("base", "EUR")
                        .param("symbols", "USD")
                        .param("startDate", "2026-04-27")
                        .param("endDate", "2026-04-28"))
                .andExpect(status().isOk());
    }
}