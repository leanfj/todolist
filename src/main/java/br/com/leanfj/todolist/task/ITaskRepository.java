package br.com.leanfj.todolist.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

    List<TaskModel> findByUserId(UUID userId);

    Optional<TaskModel> findById(UUID id);

}
