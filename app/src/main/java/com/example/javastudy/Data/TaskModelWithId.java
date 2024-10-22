package com.example.javastudy.Data;

public class TaskModelWithId {

    private final Integer id;
    private final String name;
    private final String status;

    public TaskModelWithId(Integer id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return  name;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return  id;
    }

}
