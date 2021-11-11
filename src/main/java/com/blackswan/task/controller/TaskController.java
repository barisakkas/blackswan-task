package com.blackswan.task.controller;

import com.blackswan.task.exception.ResourceNotFoundException;
import com.blackswan.task.repository.UserRepository;
import com.blackswan.task.model.Task;
import com.blackswan.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}/task")
    public List<Task> getAllTasksByUserId(@PathVariable (value = "userId") Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @PostMapping("/user/{userId}/task")
    public Task createTask(@PathVariable (value = "userId") Long userId,
                                 @Valid @RequestBody Task task) throws ResourceNotFoundException {
        return userRepository.findById(userId).map(user -> {
            task.setUser(user);
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @PutMapping("/user/{userId}/task/{taskId}")
    public Task updateTask(@PathVariable (value = "userId") Long userId,
                                 @PathVariable (value = "taskId") Long taskId,
                                 @Valid @RequestBody Task taskRequest) throws ResourceNotFoundException {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return taskRepository.findById(taskId).map(task -> {
            task.setName(taskRequest.getName() == null ? task.getName() : taskRequest.getName());
            task.setDescription(taskRequest.getDescription() == null ? task.getDescription() : taskRequest.getDescription());
            task.setDate_time(taskRequest.getDate_time() == null ? task.getDate_time() : taskRequest.getDate_time());
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("TaskId " + taskId + "not found"));
    }

    @DeleteMapping("/user/{userId}/task/{taskId}")
    public ResponseEntity<?> deleteComment(@PathVariable (value = "userId") Long userId,
                                           @PathVariable (value = "taskId") Long taskId) throws ResourceNotFoundException {
        return taskRepository.findByIdAndUserId(taskId, userId).map(comment -> {
            taskRepository.delete(comment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId + " and userId " + userId));
    }

    @GetMapping("/user/{userId}/task/{taskId}")
    public ResponseEntity<Task> getAllTasksByUserId(@PathVariable (value = "userId") Long userId,
                                          @PathVariable (value = "taskId") Long taskId) throws ResourceNotFoundException {
        Task task = taskRepository.findByIdAndUserId(taskId, userId).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId + " and userId " + userId));

        return ResponseEntity.ok().body(task);

    }
}
