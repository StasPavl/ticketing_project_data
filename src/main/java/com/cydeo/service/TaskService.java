package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;

import java.util.List;

public interface TaskService {
    List<TaskDTO> listAllTasks();
    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);
    void complete(TaskDTO dto);

    TaskDTO getTaskByID(Long id);
    int totalNonCompletedTask(String projectCode);
    int totalCompletedTask(String projectCode);
    List<TaskDTO> listOfAllNonCompletedTask();
    List<TaskDTO> listOfAllCompletedTask();

    void updateStatus(TaskDTO dto);
}
