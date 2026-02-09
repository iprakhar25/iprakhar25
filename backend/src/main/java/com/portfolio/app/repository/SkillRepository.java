package com.portfolio.app.repository;

import com.portfolio.app.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByOrderBySkillOrderAsc();
    List<Skill> findByCategory(String category);
}
