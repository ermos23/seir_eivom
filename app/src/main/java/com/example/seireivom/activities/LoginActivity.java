package com.example.seireivom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seireivom.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login =  findViewById(R.id.loginBtn);
        Button register = findViewById(R.id.registerBtn);
        login.setOnClickListener(view -> {
            //login using SQLite credentials
            startActivity(new Intent(this, MainActivity.class));
        });
        register.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}