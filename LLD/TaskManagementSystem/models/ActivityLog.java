package LLD.TaskManagementSystem.models;

import java.time.LocalDateTime;

public class ActivityLog {

    private final String description;
    private final LocalDateTime timeStamp;
    public ActivityLog(String description) {
            this.description = description;
            this.timeStamp = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "[" + timeStamp + "] " + description;
    }
}
