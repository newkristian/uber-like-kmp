package com.example.uberapp_tim9.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.main_page.DriverMainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void openDriverMainActivity(View view)
    {
        startActivity(new Intent(LoginActivity.this, DriverMainActivity.class));
    }
}