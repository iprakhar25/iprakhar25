package com.portfolio.app.service;

import com.portfolio.app.dto.VisitorCountResponse;
import com.portfolio.app.entity.Visitor;
import com.portfolio.app.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    public VisitorCountResponse getVisitorCount() {
        Long count = visitorRepository.count();
        return VisitorCountResponse.builder()
                .totalVisitors(count)
                .build();
    }

    public void incrementVisitorCount(String ipAddress, String userAgent) {
        Optional<Visitor> existing = visitorRepository.findByIpAddress(ipAddress);

        if (existing.isEmpty()) {
            Visitor visitor = Visitor.builder()
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .build();
            visitorRepository.save(visitor);
        }
    }
}
