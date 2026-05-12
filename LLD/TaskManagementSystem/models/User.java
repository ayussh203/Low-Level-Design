package LLD.TaskManagementSystem.models;

import java.util.UUID;

public class User {
    private final String id;
    private String name;
    private String email;

    public User(String name,String email)
    {
        this.id=UUID.randomUUID().toString();
        this.name=name;
        this.email=email;
    }
    //getters and setters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
