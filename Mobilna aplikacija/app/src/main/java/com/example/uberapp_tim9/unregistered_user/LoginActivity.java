package com.example.uberapp_tim9.unregistered_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.passenger.PassengerMainActivity;
import com.example.uberapp_tim9.unregistered_user.registration.RegisterFirstActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button login = findViewById(R.id.close_panic_overlay_button);
        login.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), PassengerMainActivity.class)));

        Button register = findViewById(R.id.sign_up_button);
        register.setOnClickListener(view ->
        {startActivity(new Intent(getApplicationContext(), RegisterFirstActivity.class));
                });

        TextView reset_password_link = findViewById(R.id.reset_password_link);
        reset_password_link.setOnClickListener(view ->
        {startActivity(new Intent(getApplicationContext(), PasswordResetActivity.class));
        });
    }

}