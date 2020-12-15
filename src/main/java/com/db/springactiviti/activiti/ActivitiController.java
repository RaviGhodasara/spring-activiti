package com.db.springactiviti.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ActivitiController {
    private static final Logger logger = LoggerFactory.getLogger(ActivitiController.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/start-process")
    public String startProcess() {
        runtimeService.startProcessInstanceByKey("my-process");
        return "Process started. Number of currently running process instances = " + runtimeService.createProcessInstanceQuery()
          .count();
    }

    @GetMapping("/get-task")
    public List<TaskRepresentation> getTasks() {
        List<Task> usertasks = taskService.createTaskQuery()
          .list();
        return usertasks.stream()
          .map(task -> new TaskRepresentation(task.getId(), task.getName(), task.getProcessInstanceId()))
          .collect(Collectors.toList());
    }

    @GetMapping("/complete-task/{taskId}")
    public void completeTaskA(@PathVariable String taskId) {
        taskService.complete(taskId);
        logger.info("Task completed");
    }
}