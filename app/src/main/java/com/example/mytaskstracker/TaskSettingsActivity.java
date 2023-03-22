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
        initSettings();
        initSortByClick();
        initSortOrderClick();
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.notesButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TaskSettingsActivity.this, TasksListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.settingsButton);
        ibSettings.setEnabled(false);
    }


    private void initSettings() {

        String sortBy = getSharedPreferences("MyTaskListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "tasksubject");
        String sortOrder = getSharedPreferences("MyTaskListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");

        RadioButton rbTitle = findViewById(R.id.radioTitle);
        RadioButton rbDescription = findViewById(R.id.radioDescription);

        if (sortBy.equalsIgnoreCase("tasksubject")) {
            rbTitle.setChecked(true);
        } else {
            rbDescription.setChecked(true);
        }


        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);

        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        } else {
            rbDescending.setChecked(true);
        }
    }

    private void initSortByClick() {

        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton rbTitle = findViewById(R.id.radioTitle);

                if (rbTitle.isChecked()) {
                    getSharedPreferences("MyTaskListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "tasksubject").apply();
                } else {
                    getSharedPreferences("MyTaskListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "taskdescription").apply();
                }
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton rbAscending = findViewById(R.id.radioAscending);

                if (rbAscending.isChecked()) {

                    getSharedPreferences("MyTaskListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "ASC").apply();
                }
                else {
                    getSharedPreferences("MyTaskListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "DESC").apply();
                }
            }
        });
    }
}