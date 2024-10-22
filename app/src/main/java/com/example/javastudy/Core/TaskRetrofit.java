package com.example.javastudy.Core;
import com.example.javastudy.Data.TaskModel;
import com.example.javastudy.Data.TaskModelWithId;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;


public interface TaskRetrofit {

    @GET("gettasks.php")
    Call<List<TaskModel>> getTasks();

    @POST("savetask.php")
    Call<TaskModel> postTask(@Body TaskModel task);

    @POST("updatetask.php")
    Call<Void> updateTask(@Body TaskModelWithId task);

}
