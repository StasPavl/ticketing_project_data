package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
      return projectRepository.findAll().stream().map(projectMapper::convertToDto)
              .collect(Collectors.toList());
    }


    @Override
    public void update(ProjectDTO dto) {

        Project project1 = projectRepository.findByProjectCode(dto.getProjectCode());//took user from db with id

        Project convertedProject = projectMapper.convertToEntity(dto);//convert userDTo to entity

        convertedProject.setId(project1.getId());//set id from ser in db to updated user

        convertedProject.setProjectStatus(project1.getProjectStatus());

        projectRepository.save(convertedProject); //save updated user to db with id
    }

    @Override
    public void delete(String code) {

        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);
        projectRepository.save(project);
    }

    @Override
    public void complete(ProjectDTO dto) {
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project project = projectMapper.convertToEntity(dto);
        projectRepository.save(project);
    }

    @Override
    public ProjectDTO findByCode(String code) {
       return projectMapper.convertToDto( projectRepository.findByProjectCode(code));
    }
}
