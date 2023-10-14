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
import lombok.ToString;

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

    public void setTitle(String title) throws Exception {

        if (title.length() > 50)
            throw new IllegalArgumentException("Title must be less than 50 characters");

        this.title = title;
    }

    public String getStatus() {
        return this.status.toString();
    }
}
