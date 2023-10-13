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

        taskModel.setStatus(TaskStatus.PENDING);
        taskModel.setUserId(UUID.fromString(request.getAttribute("userId").toString()));

        var task = this.taskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }


    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {

        var userId = request.getAttribute("userId");

        var tasks = this.taskRepository.findByUserId((UUID) userId);

        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable("id") UUID id) {
        
        
        var task = this.taskRepository.findById((UUID) id);
        
        if(task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }


        // private String title;
        // private String description;
        // private LocalDateTime updatedAt;
        // private String priority;

        if(taskModel.getTitle() != null) {
            task.get().setTitle(taskModel.getTitle());
        }

        if(taskModel.getDescription() != null) {
            task.get().setDescription(taskModel.getDescription());
        }

        if(taskModel.getPriority() != null) {
            task.get().setPriority(taskModel.getPriority());
        }

        task.get().setUpdatedAt(LocalDateTime.now());

        this.taskRepository.save(task.get());

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }


    @PutMapping("/{id}/start")
    public ResponseEntity start(HttpServletRequest request, @PathVariable("id") UUID id) {

        var task = this.taskRepository.findById((UUID) id);
        if(task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        if(task.get().getStatus() != TaskStatus.PENDING) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task is not pending");
        }

        task.get().start();

        this.taskRepository.save(task.get());

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity finish(HttpServletRequest request, @PathVariable("id") UUID id) {
        
        
        var task = this.taskRepository.findById((UUID) id);
        
        if(task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }


        if(task.get().getStatus() != TaskStatus.STARTED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task is not started");
        }
 
        task.get().finish();

        this.taskRepository.save(task.get());

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }
}
