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
    private static View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public TaskAdapter(ArrayList<Task> arrayList, Context context) {
        taskData = arrayList;
        parentContext = context;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TaskViewHolder cvh = (TaskViewHolder) holder;
        cvh.getTaskTextView().setText(taskData.get(position).getSubject());
        cvh.getTextDescription().setText(taskData.get(position).getDescription());
        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }
    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position){
        Task task = taskData.get(position);
        TaskDataSource ds = new TaskDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteTask(task.getTaskID());
            ds.close();
            if (didDelete) {
                taskData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTask;
        public TextView textDescription;
        public Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.textTask);
            textDescription = itemView.findViewById(R.id.textDescription);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

        public TextView getTaskTextView() {

            return textViewTask;
        }

        public TextView getTextDescription() {

            return textDescription;
        }
    }


    public static void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TaskViewHolder(v);
    }


    @Override
    public int getItemCount () {

        return taskData.size();
    }


}