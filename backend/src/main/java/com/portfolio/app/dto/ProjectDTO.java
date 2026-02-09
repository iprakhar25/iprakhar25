package com.portfolio.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String shortDescription;
    private List<String> techStack;
    private String githubUrl;
    private String liveUrl;
    private String imageUrl;
    private Integer order;
}
