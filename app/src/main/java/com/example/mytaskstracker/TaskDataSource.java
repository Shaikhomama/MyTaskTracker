package com.example.mytaskstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskDataSource {

    private SQLiteDatabase database;
    private TaskDBHelper dbHelper;

    public TaskDataSource(Context context) {

        dbHelper = new TaskDBHelper(context);
    }


    public void open() throws SQLException {

        database = dbHelper.getWritableDatabase();
    }


    public void close() {

        dbHelper.close();
    }


    public boolean insertTask(Task t) {

        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("tasksubject", t.getSubject());
            initialValues.put("taskdescription", t.getDescription());
            initialValues.put("taskduedate", String.valueOf(t.getDueDate().getTimeInMillis()));

            didSucceed = database.insert("task", null, initialValues) > 0;
        } catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateTask(Task t) {

        boolean didSucceed = false;
        try {
            Long rowId = (long) t.getTaskID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("tasksubject", t.getTaskID());
            updateValues.put("taskdescription", t.getDescription());
            updateValues.put("taskduedate", String.valueOf(t.getDueDate().getTimeInMillis()));


            didSucceed = database.update("task", updateValues, "_id =" + rowId, null) > 0;
        } catch (Exception e) {
            //do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastTaskID() {
        int lastId;

        try {
            String query = "Select MAX(_id) from task";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        } catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<String> getTaskName() {

        ArrayList<String> taskNames = new ArrayList<>();

        try {
            String query = "Select taskname from task";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                taskNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            taskNames = new ArrayList<String>();
        }
        return taskNames;
    }

    public ArrayList<Task> getTasks(String sortField, String sortOrder) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        try  {
            String query = "SELECT * FROM task ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Task newTask;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                newTask = new Task();
                newTask.setTaskID(cursor.getInt(0));
                newTask.setSubject(cursor.getString(1));
                newTask.setDescription(cursor.getString(2));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(3)));
                newTask.setDueDate(calendar);
                tasks.add(newTask);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            tasks = new ArrayList<Task>();
        }
        return tasks;
    }

    public Task getSpecificTask(int taskId){
        Task task = new Task();
        String query = "SELECT * FROM task WHERE _id=" + taskId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            task.setTaskID(cursor.getInt(0));
            task.setSubject(cursor.getString(1));
            task.setDescription(cursor.getString(2));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(3)));
            task.setDueDate(calendar);

            cursor.close();
        }
        return task;
    }

    public boolean deleteTask(int taskId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("task", "_id=" + taskId, null) > 0;
        }
        catch (Exception e) {
            //do nothing value is already false
        }
        return didDelete;
    }

}
