package com.example.javastudy.Views.NewTask;


import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.javastudy.Core.ApiClient;
import com.example.javastudy.Core.TaskRetrofit;
import com.example.javastudy.Data.TaskModel;
import com.example.javastudy.R;
import com.example.javastudy.Utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AddNewTaskFragment extends Fragment {
    private EditText taskName;
    private Button addTask, backButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_task, container, false);

        EditText taskTitleEditText = view.findViewById(R.id.taskTitleEditText);
        Button submitButton = view.findViewById(R.id.submitButton);
        Button backButton = view.findViewById(R.id.backButton);

        NavController navController = findNavController(requireActivity(), R.id.nav_host_fragment);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = taskTitleEditText.getText().toString();
                if (taskTitle.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter a task name", Toast.LENGTH_SHORT).show();
                } else {
                    postData(taskTitle, "pending", navController);
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!navController.navigateUp()) {
                }
            }
        });

        return view;
    }

    private void postData(String name, String status, NavController navController) {
        Retrofit retrofit = ApiClient.getInstance(Constants.taskAPI).getRetrofit();
        TaskRetrofit taskRetrofit = retrofit.create(TaskRetrofit.class);
        Call<TaskModel> call = taskRetrofit.postTask(new TaskModel(name, status));


        call.enqueue(new Callback<TaskModel>() {

            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                Toast.makeText(requireContext(), "Task added successfully", Toast.LENGTH_SHORT).show();
                if (!navController.navigateUp()) {
                }
            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                if (!navController.navigateUp()) {
                    Toast.makeText(requireContext(), "Error adding task", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}

