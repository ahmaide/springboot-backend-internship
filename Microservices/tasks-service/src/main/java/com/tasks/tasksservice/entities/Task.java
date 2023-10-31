package com.tasks.tasksservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "complete")
    private boolean complete;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "from_date")
    private Date from;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "to_date")
    private Date to;

    @Column(name = "userId")
    private String userId;

}