package com.portfolio.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeOnEarthResponse {
    private Long birthTimestamp; // Milliseconds since epoch
    private Long currentTimestamp;
    private Long totalSeconds;
    private Long totalMinutes;
    private Long totalDays;
    private String formattedTime; // Human-readable
}
