package cz.tul.ondrejdonat.currencyrates.service;

import cz.tul.ondrejdonat.currencyrates.exception.SettingsNotFoundException;
import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;
import cz.tul.ondrejdonat.currencyrates.repository.UserSettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SettingsService {

    private final UserSettingsRepository userSettingsRepository;

    public SettingsService(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public UserSettings saveSettings(UserSettings settings) {
        log.info("Ukladam uzivatelska nastaveni. baseCurrency={}, selectedCurrencies={}",
                settings.getBaseCurrency(), settings.getSelectedCurrencies());

        return userSettingsRepository.save(settings);
    }
    public UserSettings getSettings() {
        log.info("Nacitam uzivatelska nastaveni.");

        return userSettingsRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public UserSettings getSettingsById(Long id) {
        log.info("Nacitam uzivatelska nastaveni pro id={}", id);

        return userSettingsRepository.findById(id)
                .orElseThrow(() -> new SettingsNotFoundException("Nastaveni s id " + id + " nebylo nalezeno."));
    }
}