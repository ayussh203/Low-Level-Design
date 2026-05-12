package LLD.TaskManagementSystem.strategy;

import LLD.TaskManagementSystem.models.Task;

import java.util.Comparator;
import java.util.List;

public class SortByPriority implements TaskSortStrategy {

    @Override
    public void sort(List<Task> task)
    {
        //higher priority comes first(lower ordinal)
        task.sort(Comparator.comparing(Task::getPriority).reversed());

    }
}
