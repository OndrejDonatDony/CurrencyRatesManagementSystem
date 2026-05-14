package cz.tul.ondrejdonat.currencyrates.controller;

import cz.tul.ondrejdonat.currencyrates.model.dto.AnalysisResultDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.AverageRatesDto;
import cz.tul.ondrejdonat.currencyrates.model.dto.ExchangeRateResponse;
import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;
import cz.tul.ondrejdonat.currencyrates.service.AnalysisService;
import cz.tul.ondrejdonat.currencyrates.service.ExchangeRateService;
import cz.tul.ondrejdonat.currencyrates.service.SettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {
    private final ExchangeRateService exchangeRateService;
    private final AnalysisService analysisService;
    private final SettingsService settingsService;

    public ViewController(ExchangeRateService exchangeRateService,
                          AnalysisService analysisService,
                          SettingsService settingsService) {
        this.exchangeRateService = exchangeRateService;
        this.analysisService = analysisService;
        this.settingsService = settingsService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        UserSettings settings = settingsService.getSettings();

        if (settings != null) {
            model.addAttribute("base", settings.getBaseCurrency());
            model.addAttribute("symbols", settings.getSelectedCurrencies());
        } else {
            model.addAttribute("base", "EUR");
            model.addAttribute("symbols", "USD,CZK,GBP");
        }

        return "dashboard";
    }

    @PostMapping("/dashboard")
    public String loadData(@RequestParam String base,
                           @RequestParam String symbols,
                           @RequestParam(required = false) String startDate,
                           @RequestParam(required = false) String endDate,
                           Model model) {
        model.addAttribute("base", base);
        model.addAttribute("symbols", symbols);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        if (base == null || base.isBlank()) {
            model.addAttribute("error", "Zadej base měnu.");
            return "dashboard";
        }
        if (symbols == null || symbols.isBlank()) {
            model.addAttribute("error", "Zadej seznam měn.");
            return "dashboard";
        }
        ExchangeRateResponse latest =
                exchangeRateService.getLatestRates(base, symbols);

        AnalysisResultDto analysis =
                analysisService.analyzeLatestRates(base, symbols);

        model.addAttribute("latest", latest);
        model.addAttribute("analysis", analysis);

        if (startDate != null && !startDate.isBlank()
                && endDate != null && !endDate.isBlank()) {

            AverageRatesDto average =
                    analysisService.calculateAverageRates(base, symbols, startDate, endDate);

            model.addAttribute("average", average);
        }

        return "dashboard";
    }
}