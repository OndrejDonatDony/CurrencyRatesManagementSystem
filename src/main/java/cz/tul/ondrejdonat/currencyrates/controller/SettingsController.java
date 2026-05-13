package cz.tul.ondrejdonat.currencyrates.controller;

import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;
import cz.tul.ondrejdonat.currencyrates.service.SettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    @GetMapping("/settings")
    public String settings(Model model) {

        UserSettings settings = settingsService.getSettings();

        if (settings == null) {
            settings = new UserSettings();
            settings.setBaseCurrency("EUR");
            settings.setSelectedCurrencies("USD,CZK,GBP");
        }

        model.addAttribute("settings", settings);

        return "settings";
    }
    @PostMapping("/settings")
    public String saveSettings(@RequestParam String baseCurrency,
                               @RequestParam String selectedCurrencies,
                               Model model) {

        UserSettings settings = settingsService.getSettings();

        if (settings == null) {
            settings = new UserSettings();
        }
        settings.setBaseCurrency(baseCurrency);
        settings.setSelectedCurrencies(selectedCurrencies);

        settingsService.saveSettings(settings);

        model.addAttribute("settings", settings);
        model.addAttribute("success", "Nastavení bylo uloženo.");

        return "settings";
    }
}