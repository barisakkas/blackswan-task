package com.blackswan.task.scheduled;

import com.blackswan.task.model.Task;
import com.blackswan.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableAsync
public class ScheduledTask {

    @Autowired
    private TaskRepository taskRepository;

    @Async
    @Scheduled(fixedRate = 20000)
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        System.out.println(
                "Tasks which due date has passed the date " + LocalDateTime.now());
        List<Task> taskList = taskRepository.findAllWithCreationDateTimeBefore(LocalDateTime.now(), "pending");
        for (int i=0; i<taskList.size(); i++){
            System.out.println(taskList.get(i));
            taskList.get(i).setStatus("completed");
            taskRepository.save(taskList.get(i));
        }
    }

}