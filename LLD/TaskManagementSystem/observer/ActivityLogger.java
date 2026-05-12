package LLD.TaskManagementSystem.observer;

import LLD.TaskManagementSystem.models.Task;

import java.util.Observer;

public class ActivityLogger implements TaskObserver {

@Override
    public void update(Task task,String changeType)
{
    System.out.println("Logger:"+task.getTitle() +"was update. Change:"+changeType );
}
}
