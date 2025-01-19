package com.learning.ToDo_rest.service;

import com.learning.ToDo_rest.data.Task;
import com.learning.ToDo_rest.data.TaskStatus;
import com.learning.ToDo_rest.exception.exceptions.InternalErrorsException;
import com.learning.ToDo_rest.logger.Log;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private Long counter = 1L;

    private final List<Task> taskList;

    public TaskService() {
        taskList = new ArrayList<>();
    }

    public Optional<Task> addTask (Task task) {
        Long id = getNextCounter();
        Log.logger.info("ID: " + id);
        task.setId(id);
        if (task.getEstado() == null) {
            task.setEstado(TaskStatus.PENDING);
        }

        boolean is_added = taskList.add(task);

        if (is_added) {
            return Optional.of(task);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Task> updateTask(Long id, Task task) {
        if (id == null) return Optional.empty();
        if (!task.getId().equals(id)){
            return Optional.empty();
        }

        boolean updated = false;

        try {
            for (Task tasks : taskList) {
                if (tasks.getId() == id) {

                    updated = true;
                    Log.logger.info("TASK:" + tasks + " modificado correctamente a " + task);
                    tasks = task;
                }
            }
        } catch (Exception e){
            throw new InternalErrorsException("Un id de las tasks creadas no es un número");
        }

        if (!updated) return Optional.empty();

        return Optional.of(task);
    }

    private Long getNextCounter(){
        return counter ++;
    }

    public Optional<List<Task>> getTasks() {
        return Optional.of(taskList);
    }

    public Optional<Task> deleteTask(Long id) {
        if (id == null) {
            return Optional.empty();
        } else {
            Task removed = taskList.remove(id.intValue() - 1);
            return Optional.of(removed);
        }
    }
}
