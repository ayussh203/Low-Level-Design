package LLD.TaskManagementSystem.state;

import LLD.TaskManagementSystem.enums.TaskStatus;
import LLD.TaskManagementSystem.models.Task;

public interface TaskState {
    void startProgress(Task task);
    void completeProgress(Task task);
    void reopenTask(Task task);
    TaskStatus getStatus();
}
