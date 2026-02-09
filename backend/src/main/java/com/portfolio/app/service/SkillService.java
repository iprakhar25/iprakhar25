package com.portfolio.app.service;

import com.portfolio.app.dto.SkillDTO;
import com.portfolio.app.entity.Skill;
import com.portfolio.app.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAllByOrderBySkillOrderAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SkillDTO> getSkillsByCategory(String category) {
        return skillRepository.findByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SkillDTO convertToDTO(Skill skill) {
        return SkillDTO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .proficiency(skill.getProficiency())
                .order(skill.getSkillOrder())
                .build();
    }
}
