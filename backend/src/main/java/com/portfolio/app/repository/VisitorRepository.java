package com.portfolio.app.repository;

import com.portfolio.app.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Optional<Visitor> findByIpAddress(String ipAddress);
    Long countBy();
}
