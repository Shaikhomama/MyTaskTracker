package com.example.mytaskstracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter {
    private ArrayList<Task> taskData;
    private View.OnClickListener mOnItemClickListener;
    private Boolean isDeleting;
    private Context parentTask;

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTask;
        public Button deleteButton;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.textSubject);
            deleteButton = itemView.findViewById(R.id.buttonDeleteTask);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getSubjectTextView() {
            return textViewTask;
        }
        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public TaskAdapter(ArrayList<Task> arrayList, Context context) {
        taskData = arrayList;
        parentTask = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TaskViewHolder tvh = (TaskViewHolder) holder;
        tvh.getSubjectTextView().setText(taskData.get(position).getSubject());
        if (isDeleting) {
            tvh.getDeleteButton().setVisibility(View.VISIBLE);
            tvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        } else {
            tvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return taskData.size();
    }

    private void deleteItem(int position) {
        Task task = taskData.get(position);
        TaskDataSource ds = new TaskDataSource(parentTask);
        try {
            ds.open();
            boolean didDelete = ds.deleteTask(task.getTaskID());
            ds.close();
            if(didDelete) {
                taskData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentTask, "Delete Failed", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {}
    }

    public void setDelete(boolean b) {isDeleting = b;}
}
