package com.manager.trainingtask.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Table(name="task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="task_id")
    private int id;

    @Column(name="description")
    private String description;

    @Column(name="complete")
    private boolean complete;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "from_date")
    private Date from;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "to_date")
    private Date to;

    @Column(name="userId")
    private int userId;

    public Task(int id, String description, boolean complete, Date from, Date to, int userId) {
        this.id = id;
        this.description = description;
        this.complete = complete;
        this.from = from;
        this.to = to;
        this.userId = userId;
    }

    public Task() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", complete=" + complete +
                ", from=" + from +
                ", to=" + to +
                ", userId=" + userId +
                '}';
    }
}
