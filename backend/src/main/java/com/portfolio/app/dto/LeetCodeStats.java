package com.portfolio.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeetCodeStats {
    private int totalSolved;
    private int easySolved;
    private int mediumSolved;
    private int hardSolved;
    private int totalQuestions;
    private float acceptanceRate;
    private int ranking;
    private int contributionPoints;
    private int reputation;
    private Map<String, Integer> submissionCalendar; // Timestamp string -> count
}
