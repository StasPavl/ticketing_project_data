package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
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

        project.setProjectCode(project.getProjectCode() + "-" + project.getId());

        projectRepository.save(project);

        taskService.deleteByProject(projectMapper.convertToDto(project));
    }

    @Override
    public void complete(ProjectDTO dto) {
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        taskService.completeByProject(projectMapper.convertToDto(project));

    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project project = projectMapper.convertToEntity(dto);
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        UserDTO user = userService.findByUserName("harold@manager.com");//will come from security(right now just hard coded)
        User userEntity = userMapper.convertToEntity(user);

        List<Project> list = projectRepository.findAllByAssignedManager(userEntity);
        return list.stream().map(project -> {
            ProjectDTO obj = projectMapper.convertToDto(project);

            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
            return obj;})
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO findByCode(String code) {
       return projectMapper.convertToDto( projectRepository.findByProjectCode(code));
    }
}
