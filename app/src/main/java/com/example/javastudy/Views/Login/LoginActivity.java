package com.example.javastudy.Views.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.javastudy.Core.ApiClient;
import com.example.javastudy.Core.LoginRetrofit;
import com.example.javastudy.Data.LoginModel;
import com.example.javastudy.Data.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.javastudy.R;
import com.example.javastudy.Utils.Constants;
import com.example.javastudy.Views.Home.MainActivity;
import com.google.gson.Gson;

public class LoginActivity  extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String localEmail = email.getText().toString();
                String localPassword = password.getText().toString();
                if(localEmail.isEmpty() || localPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    login(localEmail, localPassword);
                }
            }
        });
    }

    private void login(String email, String password) {
        Retrofit retrofit = ApiClient.getInstance(Constants.loginAPI).getRetrofit();

        LoginRetrofit loginRetrofit = retrofit.create(LoginRetrofit.class);
        LoginModel loginModel = new LoginModel(email, password, 30);

        Call<UserModel> call = loginRetrofit.login(loginModel);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel userModel = response.body();
                    String myUserObject = new Gson().toJson(userModel);
                    if (userModel != null) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
                        sharedPreferences.edit().putString("user", myUserObject).apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: UserModel is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = response.message();
                    Toast.makeText(LoginActivity.this, "ERROR: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error logging in: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
