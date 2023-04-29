package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    List<ProjectDTO> listAllProjects();
    void save(ProjectDTO dto);
    void update(ProjectDTO dto);
    void delete(String code);
    void complete(ProjectDTO dto);
    ProjectDTO findByCode(String code);
    List<ProjectDTO> listAllProjectDetails();

}
