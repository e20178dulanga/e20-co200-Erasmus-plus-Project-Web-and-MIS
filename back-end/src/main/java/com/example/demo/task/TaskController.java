package com.example.demo.task;

import com.example.demo.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/tasks")

@AllArgsConstructor
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getTaskList(){
        return taskService.getAllTasks();
    }

    @GetMapping("/{taskID}")
    public Task getTask(@PathVariable Integer taskID){
        return taskService.getTask(taskID);
    }

    @GetMapping("/users/{userID}")
    public List<Task> getUserTask(@PathVariable Long userID){
        System.out.println("TASKS + "+taskService.findUserTasks(userID));
        return taskService.findUserTasks(userID);
    }

    @PostMapping
    public TaskResponse addTask(@RequestBody Task newTask){
        taskService.addTask(newTask);
        String s = "SUCCESS: new Task added";
        return new TaskResponse(s);
    }

    @PostMapping("/addWithUsers")
    public ResponseEntity<String> addTaskWithUsers(@RequestBody TaskDTO taskDTO){
        try {
            Task task = taskDTO.getTask();
            Set<AppUser> taskMembers = taskDTO.getTaskMembers();
            taskService.addTaskWithUsers(task,taskMembers);
            return ResponseEntity.ok("Task added successfully");
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to add task");
        }
    }

    @PostMapping("/updateWithUsers")
    public ResponseEntity<String> updateTaskWithUsers(@RequestBody TaskDTO taskDTO){
        try {
            Task task = taskDTO.getTask();
            Set<AppUser> taskMembers = taskDTO.getTaskMembers();
            taskService.updateTaskWithUsers(task,taskMembers);
            return ResponseEntity.ok("Task updated successfully");
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update task");
        }
    }

    @PutMapping("/update")
    public void updateTask(@RequestBody Task updateTask){
        taskService.updateTask(updateTask);
    }

    @DeleteMapping("/{taskID}")
    public void deleteTask(@PathVariable Integer taskID){
        taskService.deleteTask(taskID);
    }

    @PutMapping("/{taskID}/users/{userID}")
    public TaskResponse addUserToTask(@PathVariable Integer taskID,@PathVariable Long userID){
        taskService.addUserToTask(taskID,userID);
        return new TaskResponse("Assigned user with id "+userID+" to task with id "+taskID);
    }

    @DeleteMapping("/{taskID}/users/{userID}")
    public TaskResponse deleteUserToTask(@PathVariable Integer taskID,@PathVariable Long userID){
        taskService.deleteTaskUsers(taskID,userID);
        return new TaskResponse("Deleted user with id "+userID+" to task with id "+taskID);
    }

}
