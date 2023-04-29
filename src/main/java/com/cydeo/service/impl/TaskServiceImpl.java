package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    private final TaskMapper taskMapper;
    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> listTask = taskRepository.findAll();
        return listTask.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = taskMapper.convertToEntity(dto);
        taskRepository.save(task);

    }

    @Override
    public void update(TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(dto.getId());
        Task convertedTask = taskMapper.convertToEntity(dto);

        convertedTask.setTaskStatus(task.get().getTaskStatus());
        convertedTask.setAssignedDate(task.get().getAssignedDate());
        taskRepository.save(convertedTask);

    }

    @Override
    public void delete(Long id) {

        Optional<Task> foundTask = taskRepository.findById(id);

        if(foundTask.isPresent()){
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }
    }

    @Override
    public TaskDTO getTaskByID(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return taskMapper.convertToDto(task.get());
    }

    @Override
    public void complete(TaskDTO dto) {

    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public List<TaskDTO> listOfAllNonCompletedTask() {
        List<Task> tasks = taskRepository.allNonCompletedTask();
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listOfAllCompletedTask() {
        List<Task> tasks = taskRepository.allCompletedTask();
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void updateStatus(TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(dto.getId());//finding task in db
        Task convertedTask  = taskMapper.convertToEntity(dto);//converting to DTO

        if(task.isPresent()){
            convertedTask.setTaskStatus(dto.getTaskStatus() == null ? task.get().getTaskStatus() : dto.getTaskStatus());//if my task requst status null, my new dto task will
            //same status as my task from db, if not null my new DTO task will get status from UI
            convertedTask.setAssignedDate(task.get().getAssignedDate());//assign same date as my entity task
            taskRepository.save(convertedTask);//saving my new taskDTO with new status and date
        }

    }
}
