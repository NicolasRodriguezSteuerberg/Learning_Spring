package com.learning.ToDo_rest.data;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class Task {
    @Null
    Long id;
    @NotNull
    String description;
    @Null
    TaskStatus estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getEstado() {
        return estado;
    }

    public void setEstado(TaskStatus estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", estado=" + estado +
                '}';
    }
}
