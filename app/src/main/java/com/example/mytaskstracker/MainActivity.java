package com.example.mytaskstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListButton();
        initSettingsButton();
        initToggleButton();
        setForEditing(false);
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            initTask(extras.getInt("taskID"));
        }
        else {
            currentTask = new Task();
        }
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.notesButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TasksListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.settingsButton);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editTask = findViewById(R.id.editTask);
        EditText editDescription = findViewById(R.id.editTextDescription);
        Button saveButton = findViewById(R.id.buttonSave);
        Button changeButton = findViewById(R.id.buttonChange);

        editTask.setEnabled(enabled);
        editDescription.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        changeButton.setEnabled(enabled);
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        TextView dueDate = findViewById(R.id.textDueDate);
        dueDate.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
    }

    private void initChangeDateButton() {
        Button changeDate = findViewById(R.id.buttonChange);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }

    private void initTextChangedEvents() {
        final EditText etTaskName = findViewById(R.id.editTask);
        etTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentTask.setSubject(etTaskName.getText().toString());
            }
        });

        final EditText etDescription = findViewById(R.id.editTextDescription);
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentTask.setDescription(etDescription.getText().toString());
            }
        });
    }

    private void initSaveButton() {

        Button saveButton = findViewById(R.id.buttonSave);
        EditText editTask= findViewById(R.id.editTask);
        EditText editDescription= findViewById(R.id.editTextDescription);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String subject = editTask.getText().toString().trim();
                String description = editDescription.getText().toString().trim();

                if (subject.isEmpty() || description.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                boolean wasSuccessful = true;
                TaskDataSource ds = new TaskDataSource(MainActivity.this);
                try {
                    ds.open();

                    if (currentTask.getTaskID() == -1) {

                        wasSuccessful = ds.insertTask(currentTask);
                        if (wasSuccessful) {

                            int newID = ds.getLastTaskID();
                            currentTask.setTaskID(newID);
                        }
                    } else {
                        wasSuccessful = ds.updateTask(currentTask);
                    }
                    ds.close();
                }
                catch(Exception e) {
                    wasSuccessful = false;
                }
                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
                if(!wasSuccessful) {
                    Toast.makeText(MainActivity.this, "failed to save", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initTask(int id) {

        TaskDataSource ds = new TaskDataSource(MainActivity.this);
        try {
            ds.open();
            currentTask = ds.getSpecificTask(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Task Failed", Toast.LENGTH_LONG).show();
        }

        EditText editSubject = findViewById(R.id.editTask);
        EditText editDescription = findViewById(R.id.editTextDescription);

        editSubject.setText(currentTask.getSubject());
        editDescription.setText(currentTask.getDescription());
    }
}