package com.portfolio.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.app.dto.LeetCodeStats;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class LeetCodeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String LEETCODE_API_URL = "https://leetcode.com/graphql";

    public LeetCodeStats getStats(String username) {
        String query = "{\"query\":\"query getUserProfile($username: String!) { allQuestionsCount { difficulty count } matchedUser(username: $username) { submitStats { acSubmissionNum { difficulty count } } submissionCalendar } }\",\"variables\":{\"username\":\"" + username + "\"}}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(query, headers);

        try {
            String response = restTemplate.postForObject(LEETCODE_API_URL, entity, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.path("data");
            JsonNode matchedUser = data.path("matchedUser");

            if (matchedUser.isMissingNode()) {
                throw new RuntimeException("User not found");
            }

            JsonNode acSubmissionNum = matchedUser.path("submitStats").path("acSubmissionNum");
            
            int total = 0, easy = 0, medium = 0, hard = 0;
            for (JsonNode node : acSubmissionNum) {
                String difficulty = node.path("difficulty").asText();
                int count = node.path("count").asInt();
                if (difficulty.equals("All")) total = count;
                else if (difficulty.equals("Easy")) easy = count;
                else if (difficulty.equals("Medium")) medium = count;
                else if (difficulty.equals("Hard")) hard = count;
            }

            String calendarStr = matchedUser.path("submissionCalendar").asText();
            Map<String, Integer> calendar = new HashMap<>();
            if (calendarStr != null && !calendarStr.isEmpty()) {
                JsonNode calendarJson = objectMapper.readTree(calendarStr);
                Iterator<Map.Entry<String, JsonNode>> fields = calendarJson.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    calendar.put(entry.getKey(), entry.getValue().asInt());
                }
            }

            return LeetCodeStats.builder()
                    .totalSolved(total)
                    .easySolved(easy)
                    .mediumSolved(medium)
                    .hardSolved(hard)
                    .submissionCalendar(calendar)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
