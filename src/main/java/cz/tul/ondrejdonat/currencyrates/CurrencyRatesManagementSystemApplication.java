package cz.tul.ondrejdonat.currencyrates;

import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;
import cz.tul.ondrejdonat.currencyrates.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class CurrencyRatesManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyRatesManagementSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(SettingsService settingsService) {
        return args -> {
            log.info("Kontroluji existenci uzivatelskeho nastaveni.");

            if (settingsService.getSettings() == null) {
                log.info("Vytvarim vychozi nastaveni.");

                UserSettings settings = new UserSettings(null, "EUR", "USD,CZK,GBP");
                settingsService.saveSettings(settings);
            } else {
                log.info("Uzivatelske nastaveni jiz existuje.");
            }
        };
    }
}