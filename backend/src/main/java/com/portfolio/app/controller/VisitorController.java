package com.portfolio.app.controller;

import com.portfolio.app.dto.VisitorCountResponse;
import com.portfolio.app.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @GetMapping("/count")
    public ResponseEntity<VisitorCountResponse> getVisitorCount() {
        VisitorCountResponse response = visitorService.getVisitorCount();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/increment")
    public ResponseEntity<Void> incrementVisitor(HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        visitorService.incrementVisitorCount(ipAddress, userAgent);
        return ResponseEntity.ok().build();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
