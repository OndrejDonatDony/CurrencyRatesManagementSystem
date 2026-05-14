package cz.tul.ondrejdonat.currencyrates.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {
    @Id
    private Long id;
    private String baseCurrency;
    private String selectedCurrencies;
}