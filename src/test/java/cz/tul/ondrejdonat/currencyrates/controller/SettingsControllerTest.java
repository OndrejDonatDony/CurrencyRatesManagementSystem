package cz.tul.ondrejdonat.currencyrates.controller;

import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettingsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SettingsService settingsService;

    @MockitoBean
    private LogService logService;

    @Test
    public void testGetSettings() throws Exception {

        when(settingsService.getSettings())
                .thenReturn(new UserSettings(1L, "EUR", "USD,CZK"));

        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSettings() throws Exception {

        when(settingsService.getSettings())
                .thenReturn(new UserSettings(1L, "EUR", "USD,CZK"));

        mockMvc.perform(post("/settings")
                        .param("baseCurrency", "EUR")
                        .param("selectedCurrencies", "USD,CZK"))
                .andExpect(status().isOk());
    }
}