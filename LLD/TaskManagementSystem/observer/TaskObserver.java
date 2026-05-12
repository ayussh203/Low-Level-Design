package LLD.TaskManagementSystem.observer;

import LLD.TaskManagementSystem.models.Task;

public interface TaskObserver {

    void update(Task task,String changeType);
}
