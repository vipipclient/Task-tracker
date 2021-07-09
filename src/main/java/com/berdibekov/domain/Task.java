package com.berdibekov.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "tasks")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Status status = Status.Open;

    @ManyToOne
    private User user;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_task_id")
    @JsonIgnore
    private Task parentTask;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "parentTask", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Task> subTasks;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public void closeTaskAndAllSubTasks() {
        status = Status.Closed;
        this.getSubTasks().forEach(Task::closeTaskAndAllSubTasks);
    }

    @Override

    public String toString() {
        return "Task{" +
               "id=" + id +
               ", user=" + user +
               ", parentTask=" + parentTask +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", project=" + project +
               '}';
    }
}
