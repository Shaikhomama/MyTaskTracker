package com.example.mytaskstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TaskSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_settings);

        initListButton();
        initSettingsButton();
        initSortByClick();
        initSortOrderClick();
    }

    private void initListButton(){                                   //connects to ContactListActivity
        ImageButton ibList = findViewById(R.id.notesButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent(TaskSettingsActivity.this, TasksListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton(){                              //connects to ContactSettingsActivity
        ImageButton ibSettings = findViewById(R.id.settingsButton);
        ibSettings.setEnabled(false);
    }

    private void initSettings(){
        String sortBy = getSharedPreferences("MyTasksTrackerPreferences",
                Context.MODE_PRIVATE).getString("sortfield","duedate");
        String sortOrder = getSharedPreferences("MyTasksTrackerPreferences",
                Context.MODE_PRIVATE).getString("sortorder","ASC");

        RadioButton rbPriority = findViewById(R.id.radioPriority);
        RadioButton rbDate = findViewById(R.id.radioDate);

        if (sortBy.equalsIgnoreCase("priority")){
            rbPriority.setChecked(true);
        }
        else{
            rbDate.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);

        if(sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);
        }
    }

    private void initSortByClick(){
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbPriority = findViewById(R.id.radioPriority);
                RadioButton rbDate = findViewById(R.id.radioDate);

                if (rbPriority.isChecked()){
                    getSharedPreferences("MyTasksTrackerPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield","priority").apply();
                }
                else{
                    getSharedPreferences("MyTasksTrackerPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "duedate").apply();
                }
            }
        });
    }

    private void initSortOrderClick(){
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if(rbAscending.isChecked()) {
                    getSharedPreferences("MyTasksTrackerPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "ASC").apply();
                }
                else {
                    getSharedPreferences("MyTasksTrackerPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "DESC").apply();
                }
            }
        });
    }
}