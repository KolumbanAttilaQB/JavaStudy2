package com.example.javastudy.Core;

import com.example.javastudy.Data.LoginModel;
import com.example.javastudy.Data.TaskModel;
import com.example.javastudy.Data.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;



public interface LoginRetrofit {
    @POST("auth/login")
    Call<UserModel> login(@Body LoginModel loginModel);
}

