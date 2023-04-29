package com.cydeo.repository;

import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
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

    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User user);
    List<Task> findAllByTaskStatusAndAssignedEmployee(Status status, User user);



    List<Task> findAllByProject(Project project);
}
