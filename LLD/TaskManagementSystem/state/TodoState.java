package LLD.TaskManagementSystem.state;

import LLD.TaskManagementSystem.enums.TaskStatus;
import LLD.TaskManagementSystem.models.Task;

public class TodoState implements TaskState{

    @Override
    public void startProgress(Task task) {
        //System.out.println("Cannot start a completed task. Reopen it first.");
        task.setState(new InProgressState());
    }
    @Override
    public void completeProgress(Task task) {
        System.out.println("Cannot complete a task that is not in progress.");
    }
    @Override
    public void reopenTask(Task task) {
        System.out.println("Task is already in TODO State");
    }
    @Override
    public TaskStatus getStatus() { return TaskStatus.DONE; }
}
