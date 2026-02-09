package com.portfolio.app.controller;

import com.portfolio.app.dto.LeetCodeStats;
import com.portfolio.app.service.LeetCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leetcode")
public class LeetCodeController {

    @Autowired
    private LeetCodeService leetCodeService;

    @GetMapping("/stats")
    public ResponseEntity<LeetCodeStats> getStats(@RequestParam(defaultValue = "iprakhar25") String username) {
        LeetCodeStats stats = leetCodeService.getStats(username);
        if (stats == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stats);
    }
}
