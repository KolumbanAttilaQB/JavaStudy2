package com.example.javastudy.Data;

public class TaskModel {

    private final Integer id = null;
    private final String name;
    private final String status;

    public TaskModel(String name, String status) {
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
