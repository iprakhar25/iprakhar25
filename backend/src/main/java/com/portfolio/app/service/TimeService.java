package com.portfolio.app.service;

import com.portfolio.app.dto.TimeOnEarthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TimeService {

    // Set your birth date here (format: YYYY-MM-DD)
    @Value("${portfolio.birth-date:2002-03-25}")
    private String birthDateStr;

    public TimeOnEarthResponse getTimeOnEarth() {
        LocalDate birthDate = LocalDate.parse(birthDateStr);
        LocalDateTime birthDateTime = birthDate.atStartOfDay();
        Instant birthInstant = birthDateTime.atZone(ZoneId.systemDefault()).toInstant();
        long birthTimestamp = birthInstant.toEpochMilli();

        Instant now = Instant.now();
        long currentTimestamp = now.toEpochMilli();

        Duration duration = Duration.between(birthInstant, now);

        long totalSeconds = duration.getSeconds();
        long totalMinutes = duration.toMinutes();
        long totalDays = duration.toDays();

        String formattedTime = String.format(
                "%d days, %d hours, %d minutes, %d seconds",
                duration.toDays(),
                duration.toHours() % 24,
                duration.toMinutes() % 60,
                duration.getSeconds() % 60
        );

        return TimeOnEarthResponse.builder()
                .birthTimestamp(birthTimestamp)
                .currentTimestamp(currentTimestamp)
                .totalSeconds(totalSeconds)
                .totalMinutes(totalMinutes)
                .totalDays(totalDays)
                .formattedTime(formattedTime)
                .build();
    }
}
