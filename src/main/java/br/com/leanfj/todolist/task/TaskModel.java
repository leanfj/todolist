package br.com.leanfj.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(length = 50)
    private String title;
    private String description;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    
    private TaskStatus status;
    private String priority;
    
    private UUID userId;



    public void start() {
        this.status = TaskStatus.STARTED;
        this.startedAt = LocalDateTime.now();
    }

    public void finish() {
        this.status = TaskStatus.FINISHED;
        this.finishedAt = LocalDateTime.now();
    }
}
