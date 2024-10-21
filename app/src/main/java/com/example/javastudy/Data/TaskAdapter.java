package com.example.javastudy.Data;
import com.example.javastudy.R;
import com.example.javastudy.Views.Home.HomeFragment;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<TaskModel> taskList;
    private OnTaskCheckedChangeListener onTaskCheckedChangeListener;
    public TaskAdapter(List<TaskModel> taskList, OnTaskCheckedChangeListener onTaskCheckedChangeListener) {
        this.taskList = taskList;
        this.onTaskCheckedChangeListener = onTaskCheckedChangeListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("ResourceType") View view = LayoutInflater.from(parent.getContext()).inflate(R.drawable.list_tile, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel task = taskList.get(position);
        holder.taskTitle.setText(task.getName());
        holder.tasktCheckbox.setChecked(task.getStatus().equals("completed"));


        holder.tasktCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onTaskCheckedChangeListener != null) {
                onTaskCheckedChangeListener.onTaskCheckedChange(task, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView taskTitle;
        CheckBox tasktCheckbox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            taskTitle = itemView.findViewById(R.id.list_title);
            tasktCheckbox = itemView.findViewById(R.id.task_checkbox);
        }
    }

    public interface OnTaskCheckedChangeListener {
        void onTaskCheckedChange(TaskModel task, boolean isChecked);
    }
}