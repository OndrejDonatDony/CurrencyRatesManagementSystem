package cz.tul.ondrejdonat.currencyrates;

import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;
import cz.tul.ondrejdonat.currencyrates.service.SettingsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CurrencyRatesManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyRatesManagementSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(SettingsService settingsService) {
        return args -> {

            if (settingsService.getSettings() == null) {

                UserSettings settings = new UserSettings(1L, "EUR", "USD,CZK,GBP");

                settingsService.saveSettings(settings);
            }
        };
    }
}