package cz.tul.ondrejdonat.currencyrates.service;
import cz.tul.ondrejdonat.currencyrates.exception.SettingsNotFoundException;
import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;
import cz.tul.ondrejdonat.currencyrates.repository.UserSettingsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingsServiceTest {
    @Mock
    private UserSettingsRepository userSettingsRepository;
    @InjectMocks
    private SettingsService settingsService;

    @Test
    void saveSettingsShouldSaveUserSettings() {
        UserSettings settings = new UserSettings(1L, "EUR", "USD,CZK,GBP");

        when(userSettingsRepository.save(settings)).thenReturn(settings);

        UserSettings result = settingsService.saveSettings(settings);

        assertEquals("EUR", result.getBaseCurrency());
        assertEquals("USD,CZK,GBP", result.getSelectedCurrencies());
        verify(userSettingsRepository).save(settings);
    }

    @Test
    void getSettingsShouldReturnFirstSettings() {
        UserSettings settings = new UserSettings(1L, "EUR", "USD,CZK,GBP");

        when(userSettingsRepository.findAll()).thenReturn(List.of(settings));

        UserSettings result = settingsService.getSettings();

        assertNotNull(result);
        assertEquals("EUR", result.getBaseCurrency());
        assertEquals("USD,CZK,GBP", result.getSelectedCurrencies());
    }

    @Test
    void getSettingsShouldReturnNullWhenNoSettingsExist() {
        when(userSettingsRepository.findAll()).thenReturn(List.of());

        UserSettings result = settingsService.getSettings();

        assertNull(result);
    }

    @Test
    void getSettingsByIdShouldReturnSettings() {
        UserSettings settings = new UserSettings(1L, "EUR", "USD,CZK,GBP");

        when(userSettingsRepository.findById(1L)).thenReturn(Optional.of(settings));

        UserSettings result = settingsService.getSettingsById(1L);

        assertEquals("EUR", result.getBaseCurrency());
        assertEquals("USD,CZK,GBP", result.getSelectedCurrencies());
    }

    @Test
    void getSettingsByIdShouldThrowExceptionWhenSettingsDoNotExist() {
        when(userSettingsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SettingsNotFoundException.class, () ->
                settingsService.getSettingsById(1L)
        );
    }
}