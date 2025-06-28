package com.tracking.taskservice.repository;

import com.tracking.taskservice.entity.Status;
import com.tracking.taskservice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndUserIdAndDeleted(Long id, Long userId, Boolean deleted);

    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndDeleted(Long userId, Boolean deleted);

}
