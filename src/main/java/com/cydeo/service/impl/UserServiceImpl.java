package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> userList = userRepository.findAll(Sort.by("firstName"));

        return userList.stream()
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());

    }

    @Override
    public UserDTO update(UserDTO user) {
        User user1 = userRepository.findByUserName(user.getUserName());//took user from db with id
        User convertedUser = userMapper.convertToEntity(user);//convert userDTo to entity
        convertedUser.setId(user1.getId());//set id from ser in db to updated user
        userRepository.save(convertedUser); //save updated user to db with id
        return findByUserName(user.getUserName());
    }

    @Override
    public List<UserDTO> listAllByRoles(String role) {
        List<User> users = userRepository.findByRoleDescriptionIgnoreCase(role);

        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userMapper.convertToDto(userRepository.findByUserName(username));
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));

    }

    @Override

    public void deleteByUserName(String username) {
        User user = userRepository.findByUserName(username);
        if (checkIfUserCanBeDeleted(user)){
            user.setIsDeleted(true);
            userRepository.save(user);
        }
    }

    private boolean checkIfUserCanBeDeleted(User user){
        switch (user.getRole().getDescription()){
            case "Manager":
                List<ProjectDTO> projectDToList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
                return projectDToList.size() == 0;
            case "Employee":
                List<TaskDTO> taskDToList = taskService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
                return taskDToList.size() == 0;
            default:
                return true;
        }
    }

}
