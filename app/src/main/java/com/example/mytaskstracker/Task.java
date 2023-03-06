package com.example.mytaskstracker;

import java.util.Calendar;

public class Task {

    private int taskID;
    private String subject;
    private String description;

    public Task() {
        taskID = -1;

    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
