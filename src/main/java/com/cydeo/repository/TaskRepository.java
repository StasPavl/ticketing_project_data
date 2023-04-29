package com.cydeo.repository;

import com.cydeo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("select count(t) From Task t where t.project.projectCode = ?1 and t.taskStatus <> 'COMPLETE' ")
    int totalNonCompletedTasks(String projectCode);

    @Query(value = "select count(*) from tasks t " +
            "Join projects p on t.project_id = p.id " +
            "where p.project_code = ?1 and t.task_status = 'COMPLETE'",
            nativeQuery = true)
    int totalCompletedTasks(String projectCode);

    @Query("SELECT t from Task t where t.taskStatus <> 'COMPLETE'")
    List<Task> allNonCompletedTask();

    @Query("SELECT t from Task t where t.taskStatus = 'COMPLETE'")
    List<Task> allCompletedTask();
}
