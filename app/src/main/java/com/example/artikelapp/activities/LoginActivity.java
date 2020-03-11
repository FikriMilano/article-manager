package com.example.artikelapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.artikelapp.api.RetrofitAuth;
import com.example.artikelapp.models.LoginResponse;
import com.example.artikelapp.R;
import com.example.artikelapp.storage.SharedPrefManager;
import com.example.artikelapp.utility.IMM;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

     EditText LoginUsername, LoginPassword;
     Button LoginLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginUsername = findViewById(R.id.LoginUsername);
        LoginPassword = findViewById(R.id.LoginPassword);
        LoginLogin = findViewById(R.id.LoginLogin);

        LoginLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String Token = SharedPrefManager.getInstance(this).getToken();

        if (Token != null) {
            toArticleActivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LoginLogin:
                IMM.hideKeyboard(this);

                final String username = LoginUsername.getText().toString().trim();
                final String password = LoginPassword.getText().toString().trim();
                boolean isEmptyFields = false;

                if (TextUtils.isEmpty(username)){
                    isEmptyFields = true;
                    LoginUsername.setError("Fill the username");
                }
                if (TextUtils.isEmpty(password)){
                    isEmptyFields = true;
                    LoginPassword.setError("Fill the password");
                }
                if (!isEmptyFields){
                    // Progress Dialog

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    Call<LoginResponse> call = RetrofitAuth
                            .getInstance()
                            .getApi()
                            .login(username, password);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                            try {
                                LoginResponse loginResponse = response.body();
                                String Token = loginResponse.getToken();

                                // Save Token
                                SharedPrefManager.getInstance(LoginActivity.this).saveToken(Token);

                                // Save User From Confirmed EditText, Because In The Response API There's No User
                                SharedPrefManager.getInstance(LoginActivity.this).saveUser(username, password);

                                progressDialog.dismiss();
                                toArticleActivity();

                            } catch (Exception e){
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;
        }
    }

    public void toArticleActivity(){
        Intent intent = new Intent(LoginActivity.this, ArticleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
