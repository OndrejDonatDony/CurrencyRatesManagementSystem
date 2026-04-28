package cz.tul.ondrejdonat.currencyrates.controller;

import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;
import cz.tul.ondrejdonat.currencyrates.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/settings")
    public String getSettings(Model model) {
        log.info("Oteviram stranku nastaveni.");

        UserSettings settings = settingsService.getSettingsById(1L);
        model.addAttribute("settings", settings);

        return "settings";
    }

    @PostMapping("/settings")
    public String updateSettings(
            @RequestParam String baseCurrency,
            @RequestParam String selectedCurrencies
    ) {
        log.info("Aktualizuji nastaveni. baseCurrency={}, selectedCurrencies={}",
                baseCurrency, selectedCurrencies);

        UserSettings settings = new UserSettings(1L, baseCurrency, selectedCurrencies);
        settingsService.saveSettings(settings);

        return "redirect:/settings";
    }
}