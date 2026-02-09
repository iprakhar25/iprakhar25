package com.portfolio.app.controller;

import com.portfolio.app.dto.SkillDTO;
import com.portfolio.app.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        List<SkillDTO> skills = skillService.getAllSkills();
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SkillDTO>> getSkillsByCategory(@PathVariable String category) {
        List<SkillDTO> skills = skillService.getSkillsByCategory(category);
        return ResponseEntity.ok(skills);
    }
}
