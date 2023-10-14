package br.com.leanfj.todolist.task;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.Column;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

        try {
            taskModel.setStatus(TaskStatus.PENDING);
            taskModel.setUserId(UUID.fromString(request.getAttribute("userId").toString()));

            var task = this.taskRepository.save(taskModel);

            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity list(HttpServletRequest request) {

        var userId = request.getAttribute("userId");

        var tasks = this.taskRepository.findByUserId((UUID) userId);

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request,
            @PathVariable("id") UUID id) {

        var task = this.taskRepository.findById((UUID) id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        var userId = request.getAttribute("userId");

        if (!task.get().getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Task not found for this user");
        }

        if (task.get().getStatus() == TaskStatus.FINISHED.toString()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task is finished");
        }

        if (taskModel.getTitle() != null) {
            try {
                task.get().setTitle(taskModel.getTitle());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }

        if (taskModel.getDescription() != null) {
            task.get().setDescription(taskModel.getDescription());
        }

        if (taskModel.getPriority() != null) {
            task.get().setPriority(taskModel.getPriority());
        }

        task.get().setUpdatedAt(LocalDateTime.now());

        this.taskRepository.save(task.get());

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity start(HttpServletRequest request, @PathVariable("id") UUID id) {

        var task = this.taskRepository.findById((UUID) id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        var userId = request.getAttribute("userId");

        if (!task.get().getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Task not found for this user");
        }

        if (task.get().getStatus() != TaskStatus.PENDING.toString()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task is not pending");
        }

        task.get().start();

        this.taskRepository.save(task.get());

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity finish(HttpServletRequest request, @PathVariable("id") UUID id) {

        var task = this.taskRepository.findById((UUID) id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        var userId = request.getAttribute("userId");

        if (!task.get().getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Task not found for this user");
        }

        if (task.get().getStatus() != TaskStatus.STARTED.toString()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task is not started");
        }

        task.get().finish();

        this.taskRepository.save(task.get());

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }
}
