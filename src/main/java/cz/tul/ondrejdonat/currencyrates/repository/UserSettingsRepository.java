package cz.tul.ondrejdonat.currencyrates.repository;

import cz.tul.ondrejdonat.currencyrates.model.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
}