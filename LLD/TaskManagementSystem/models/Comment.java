package LLD.TaskManagementSystem.models;

import java.util.Date;
import java.util.UUID;

public class Comment {

    private final String id;
    private final User author;
    private final String content;
    private final Date timeStamp;
    public Comment(String id, User author, String content, Date timeStamp) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.content = content;
        this.timeStamp = new Date();
    }
    public User getAuthor() {
        return author;
    }

}
