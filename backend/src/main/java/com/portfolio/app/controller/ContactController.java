package com.portfolio.app.controller;

import com.portfolio.app.dto.ContactMessageRequest;
import com.portfolio.app.service.ContactService;
import com.portfolio.app.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> sendMessage(
            @RequestHeader("Authorization") String token,
            @RequestBody ContactMessageRequest request) {
        try {
            // Extract token
            String jwtToken = token.replace("Bearer ", "");
            
            if (!jwtTokenProvider.validateToken(jwtToken)) {
                return ResponseEntity.badRequest().body("Invalid token");
            }

            String userIdStr = jwtTokenProvider.getUserIdFromToken(jwtToken);
            Long userId = Long.parseLong(userIdStr);

            contactService.sendMessage(userId, request.getSubject(), request.getMessage());
            return ResponseEntity.ok().body("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
