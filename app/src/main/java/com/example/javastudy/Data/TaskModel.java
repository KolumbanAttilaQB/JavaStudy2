package com.example.javastudy.Data;

public class TaskModel {

    private final Integer id = null;
    private final String name;
    private final String status;
    private final String image;

    public TaskModel(String name, String status, String img) {
        this.name = name;
        this.status = status;
        this.image = img;
    }

    public String getName() {
        return  name;
    }
    public String getImg() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return  id;
    }

}


