package com.example.mytaskstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TasksListActivity extends AppCompatActivity {
    ArrayList<Task> tasks;
    TaskAdapter taskAdapter = new TaskAdapter(tasks, this);
    RecyclerView taskList;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int taskId = tasks.get(position).getTaskID();
            Intent intent = new Intent(TasksListActivity.this, MainActivity.class);
            intent.putExtra("taskID", taskId);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        initListButton();
        initSettingsButton();
        initAddTaskButton();
        initDeleteSwitch();
        TaskAdapter.setOnItemClickListener(onItemClickListener);

        String sortBy = getSharedPreferences("MyTaskListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "date");
        String sortOrder = getSharedPreferences("MyTaskListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");


        TaskDataSource ds = new TaskDataSource(this);

        try {
            ds.open();
            tasks = ds.getTasks(sortBy, sortOrder);

            ds.close();

            RecyclerView taskList = findViewById(R.id.rvTasks);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

            taskList.setLayoutManager(layoutManager);

            TaskAdapter taskAdapter = new TaskAdapter(tasks, this);

            taskList.setAdapter(taskAdapter);


        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving Tasks", Toast.LENGTH_LONG).show();
        }

//        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                double batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
//                double levelScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
//                int batteryPercent  = (int) Math.floor(batteryLevel / levelScale * 100);
//                TextView textBatteryState  = findViewById(R.id.textBatteryLevel);
//                textBatteryState.setText(batteryPercent + "%");
//            }
//        };
//
//        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        registerReceiver(batteryReceiver, filter);

    }

    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("MyTaskListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "date");
        String sortOrder = getSharedPreferences("MyTaskListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");
        TaskDataSource ds = new TaskDataSource(this);
        try{
            ds.open();
            tasks = ds.getTasks(sortBy, sortOrder);
            ds.close();
            if (tasks.size() > 0 ) {
                taskList = findViewById(R.id.rvTasks);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                taskList.setLayoutManager(layoutManager);
                taskAdapter = new TaskAdapter(tasks, this);
                taskList.setAdapter(taskAdapter);
            } else {
                Intent intent = new Intent(TasksListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error Retrieving Tasks", Toast.LENGTH_LONG).show();
        }
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean status = compoundButton.isChecked();
                taskAdapter.setDelete(status);
                taskAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initListButton() {
        ImageButton ibList = findViewById(R.id.notesButton);
        ibList.setEnabled(false);
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.settingsButton);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TasksListActivity.this, TaskSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initAddTaskButton() {
        Button newTask = findViewById(R.id.buttonAddTask);
        newTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TasksListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}