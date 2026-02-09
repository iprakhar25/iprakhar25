package com.portfolio.app.service;

import com.portfolio.app.dto.ProjectDTO;
import com.portfolio.app.entity.Project;
import com.portfolio.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAllByOrderByProjectOrderAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return convertToDTO(project);
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = Project.builder()
                .title(projectDTO.getTitle())
                .description(projectDTO.getDescription())
                .shortDescription(projectDTO.getShortDescription())
                .techStack(projectDTO.getTechStack())
                .githubUrl(projectDTO.getGithubUrl())
                .liveUrl(projectDTO.getLiveUrl())
                .imageUrl(projectDTO.getImageUrl())
                .projectOrder(projectDTO.getOrder())
                .build();

        project = projectRepository.save(project);
        return convertToDTO(project);
    }

    private ProjectDTO convertToDTO(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .shortDescription(project.getShortDescription())
                .techStack(project.getTechStack())
                .githubUrl(project.getGithubUrl())
                .liveUrl(project.getLiveUrl())
                .imageUrl(project.getImageUrl())
                .order(project.getProjectOrder())
                .build();
    }
}
