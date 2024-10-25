package com.example.javastudy.Views.Home;

import static androidx.navigation.Navigation.findNavController;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javastudy.Core.ApiClient;
import com.example.javastudy.Core.TaskRetrofit;
import com.example.javastudy.Data.TaskAdapter;
import com.example.javastudy.Data.TaskModel;
import com.example.javastudy.Data.TaskModelWithId;
import com.example.javastudy.Data.UserModel;
import com.example.javastudy.R;
import com.example.javastudy.Utils.Constants;
import com.example.javastudy.Views.NewTask.AddNewTaskFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements  TaskAdapter.OnTaskCheckedChangeListener {

    TextView welcomeText;
    TaskAdapter taskAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button addNewTask;
    Button openMap;
    SearchView searchView;
    List<TaskModel> filteredTasks = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        welcomeText = view.findViewById(R.id.welcomeText);
        searchView = view.findViewById(R.id.search);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);

        String jsonString = sharedPreferences.getString("user", "");
        Gson userParser = new Gson();
        UserModel user =userParser.fromJson(jsonString, UserModel.class);
        setWelcomeMessage(user);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressbar);
        addNewTask = view.findViewById(R.id.addTask);
        openMap = view.findViewById(R.id.getLocation);

        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_mainFragment_to_mapFragment);
            }
        });

        addNewTask.setOnClickListener( new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                NavController navController = findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_mainFragment_to_addTaskFragment);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == taskAdapter.getItemCount() - 1) {
////                    taskAdapter.setLoading(true);
////                    loadMoreTasks();
//                }
            }
        });
       List<TaskModel> result = taskList();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SEARCH", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SEARCH2", newText + " ---- Results: " + result.size());

                filteredTasks.clear();

                for (TaskModel taskModel : result) {
                    if (taskModel.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredTasks.add(taskModel);
                    }
                }
                taskAdapter.notifyDataSetChanged();
                return true;
            }
        });


        return view;
    }

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    protected void setWelcomeMessage(UserModel user) {
        if (welcomeText != null) {
            welcomeText.setText("Welcome back, " + user.getUserName());
        }
    }

    protected List<TaskModel> taskList() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = ApiClient.getInstance(Constants.taskAPI).getRetrofit();
        TaskRetrofit taskRetrofit = retrofit.create(TaskRetrofit.class);
        Call<List<TaskModel>> call = taskRetrofit.getTasks();
        List<TaskModel> tasks = new ArrayList<>();
        call.enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tasks.addAll(response.body());
                    filteredTasks.addAll(tasks);

                    taskAdapter = new TaskAdapter(filteredTasks, HomeFragment.this);
                    recyclerView.setAdapter(taskAdapter);

                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.d("TASK_LIST", "Error " + response.message());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Error getting tasks: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return  tasks;
    }



    @Override
    public void onTaskCheckedChange(TaskModel task, boolean isChecked) {
        Retrofit retrofit = ApiClient.getInstance(Constants.taskAPI).getRetrofit();
        TaskRetrofit taskRetrofit = retrofit.create(TaskRetrofit.class);

        Call<Void> updateCall = taskRetrofit.updateTask(new TaskModelWithId(task.getId(),task.getName(), isChecked ? "completed" : "pending"));
        updateCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(requireContext(), "Task updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to update task: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Error updating task: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void loadMoreTasks() {
////        new Handler().postDelayed(() -> {
////            List<TaskModel> newTasks = generateMoreTasks();
////            if (newTasks.size() > 0) {
////                taskAdapter.addTasks(newTasks);
////            }
////            taskAdapter.setLoading(false);
////        }, 2000);
//    }
//
//    private List<TaskModel> generateMoreTasks() {
//        List<TaskModel> newTasks = new ArrayList<>();
//        int currentSize = taskAdapter.getItemCount();
//        for (int i = currentSize; i < currentSize + 10; i++) {
//            newTasks.add(new TaskModel("Task " + (i + 1), "pending", "https://picsum.photos/200/300"));
//        }
//        return newTasks;
//    }

}
