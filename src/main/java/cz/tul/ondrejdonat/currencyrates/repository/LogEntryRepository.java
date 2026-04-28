package cz.tul.ondrejdonat.currencyrates.repository;

import cz.tul.ondrejdonat.currencyrates.model.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
}