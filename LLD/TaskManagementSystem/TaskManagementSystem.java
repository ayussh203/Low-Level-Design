package LLD.TaskManagementSystem;

import LLD.TaskManagementSystem.enums.TaskPriority;
import LLD.TaskManagementSystem.enums.TaskStatus;
import LLD.TaskManagementSystem.models.Task;
import LLD.TaskManagementSystem.models.TaskList;
import LLD.TaskManagementSystem.models.User;
import LLD.TaskManagementSystem.observer.ActivityLogger;
import LLD.TaskManagementSystem.strategy.TaskSortStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskManagementSystem {

    private static TaskManagementSystem instance;
    private ConcurrentHashMap<String, Task> tasks;
    private ConcurrentHashMap<String, User> users;
    private ConcurrentHashMap<String, TaskList> taskLists;
    public static TaskManagementSystem getInstance() {
        if (instance == null) {
            instance = new TaskManagementSystem();
        }
        return instance;
    }
    private TaskManagementSystem() {
        tasks = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
        taskLists = new ConcurrentHashMap<>();

    }

    public User createUser(String name,String email)
    {
        User user=new User(name,email);
        users.put(user.getId(),user);
        return user;
    }
    public TaskList createTaskList(String name)
    {
TaskList  taskList=new TaskList(name);
taskLists.put(taskList.getId(), taskList);
return taskList;
    }

    public Task createTask(String description, String title, LocalDate dueDate, TaskPriority priority,String createdByUserId)
    {
        User user=users.get(createdByUserId);
        if(user==null)
        {
            throw new IllegalArgumentException("User does not exist");
        }
        Task task=new Task.TaskBuilder(title)
                .description(description)
                .dueDate(dueDate)
                .priority(priority)

                .createdBy(user)
                .build();
        //task.addObserver(new ActivityLogger());
        tasks.put(task.getId(),task);
        return task;
    }
    public List<Task> listTaskByUser(String userId)
    {
        User user=users.get(userId);
        if(user==null)
        {
            throw new IllegalArgumentException("User does not exist");
        }
        return tasks.values().stream().filter(task->user.equals(task.getAssignee())).toList();
    }
    public void deleteTask(String taskId) {
        tasks.remove(taskId);
        taskLists.remove(taskId);

    }
    public List<Task> listTasksByStatus(TaskStatus status) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }
    public List<Task> searchTasks(String keyword, TaskSortStrategy sortStrategy)
    {
        List<Task> matchingTask=new ArrayList<>();
        for(Task task:tasks.values())
        {
            if(task.getTitle().contains(keyword) || task.getDescription().contains(keyword))
            {
                matchingTask.add(task);
            }
        }
        if(matchingTask.isEmpty())
        {
            throw new IllegalArgumentException("No matching tasks found");
        }
        sortStrategy.sort(matchingTask);
        return matchingTask;
    }
}
