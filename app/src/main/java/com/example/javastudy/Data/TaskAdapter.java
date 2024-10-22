package com.example.javastudy.Data;

import com.example.javastudy.R;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_TASK = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private final List<TaskModel> taskList;
    private OnTaskCheckedChangeListener onTaskCheckedChangeListener;
    private boolean isLoading;

    public TaskAdapter(List<TaskModel> taskList, OnTaskCheckedChangeListener onTaskCheckedChangeListener) {
        this.taskList = taskList;
        this.onTaskCheckedChangeListener = onTaskCheckedChangeListener;
        this.isLoading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TASK) {
            @SuppressLint("ResourceType") View view = LayoutInflater.from(parent.getContext()).inflate(R.drawable.list_tile, parent, false);
            return new TaskViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TaskViewHolder) {
            TaskModel task = taskList.get(position);
            TaskViewHolder taskHolder = (TaskViewHolder) holder;

            taskHolder.taskTitle.setText(task.getName());
            taskHolder.tasktCheckbox.setChecked(task.getStatus().equals("completed"));

            Picasso.get().load(task.getImg()).into(taskHolder.imageView);

            taskHolder.tasktCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (onTaskCheckedChangeListener != null) {
                    onTaskCheckedChangeListener.onTaskCheckedChange(task, isChecked);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return taskList.size() + (isLoading ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == taskList.size() && isLoading) ? VIEW_TYPE_LOADING : VIEW_TYPE_TASK;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
        notifyItemChanged(taskList.size());
    }

    public void addTasks(List<TaskModel> newTasks) {
        int startPosition = taskList.size();
        taskList.addAll(newTasks);
        notifyItemRangeInserted(startPosition, newTasks.size());
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView taskTitle;
        CheckBox tasktCheckbox;
        ImageView imageView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            taskTitle = itemView.findViewById(R.id.list_title);
            tasktCheckbox = itemView.findViewById(R.id.task_checkbox);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnTaskCheckedChangeListener {
        void onTaskCheckedChange(TaskModel task, boolean isChecked);
    }
}
