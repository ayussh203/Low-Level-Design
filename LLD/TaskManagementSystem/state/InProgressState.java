package LLD.TaskManagementSystem.state;

import LLD.TaskManagementSystem.enums.TaskStatus;
import LLD.TaskManagementSystem.models.Task;

public class InProgressState implements TaskState {

    @Override
    public void startProgress(Task task) {
        System.out.println("Task is already in Progress");
    }
    @Override
    public void completeProgress(Task task) {
        //System.out.println("Task is already done.");
        task.setState(new DoneState());
    }
    @Override
    public void reopenTask(Task task) {
        task.setState(new TodoState());
    }
    @Override
    public TaskStatus getStatus() { return TaskStatus.IN_PROGRESS; }
}
