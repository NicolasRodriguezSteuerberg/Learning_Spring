package com.learning.ToDo_rest.controller;


import com.learning.ToDo_rest.data.Task;
import com.learning.ToDo_rest.service.TaskService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task")
public class TaskController {

    private TaskService taskService;

    public TaskController (TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity<?> getTasks() {
        Optional<List<Task>> allTasks = taskService.getTasks();
        if (allTasks.isPresent()) {
            return ResponseEntity.ok(allTasks.get());
        }
        return ResponseEntity.badRequest().body("No se pudo recoger");
    }

    @PostMapping()
    public ResponseEntity<?> addTask(@RequestBody Task task) {
        Optional<Task> createdTask = taskService.addTask(task);
        if (createdTask.isPresent()) {
            return ResponseEntity.ok(createdTask.get());
        } else {
            return ResponseEntity.badRequest().body("Argumentos inválidos");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable(name = "id") Long id, @RequestBody Task task) {
        Optional<Task> createdTask = taskService.updateTask(id, task);
        if (createdTask.isPresent()) {
            return ResponseEntity.ok(createdTask.get());
        } else {
            return ResponseEntity.badRequest().body("Argumentos inválidos");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletedTask(Long id){
        Optional<Task> createdTask = taskService.deleteTask(id);
        if (createdTask.isPresent()) {
            return ResponseEntity.ok(createdTask.get());
        } else {
            return ResponseEntity.badRequest().body("Argumentos inválidos");
        }
    }

}
