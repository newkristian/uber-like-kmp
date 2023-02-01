package com.example.uberapp_tim9.unregistered_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.uberapp_tim9.R;

public class PasswordResetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        Button resetPassword = findViewById(R.id.login_button);
        resetPassword.setOnClickListener(v ->
        {
            Toast.makeText(getBaseContext(),
                    "Lozinka uspešno promenjena!",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }
}