package cz.tul.ondrejdonat.currencyrates.service;

import cz.tul.ondrejdonat.currencyrates.model.entity.LogEntry;
import cz.tul.ondrejdonat.currencyrates.repository.LogEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {
    private final LogEntryRepository logEntryRepository;

    public LogService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    public void log(String level, String message) {
        LogEntry entry = new LogEntry();
        entry.setTimestamp(LocalDateTime.now());
        entry.setLevel(level);
        entry.setMessage(message);

        logEntryRepository.save(entry);
    }
}