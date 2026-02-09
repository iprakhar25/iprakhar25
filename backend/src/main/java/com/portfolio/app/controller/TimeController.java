package com.portfolio.app.controller;

import com.portfolio.app.dto.TimeOnEarthResponse;
import com.portfolio.app.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/time-on-earth")
public class TimeController {

    @Autowired
    private TimeService timeService;

    @GetMapping
    public ResponseEntity<TimeOnEarthResponse> getTimeOnEarth() {
        TimeOnEarthResponse response = timeService.getTimeOnEarth();
        return ResponseEntity.ok(response);
    }
}
