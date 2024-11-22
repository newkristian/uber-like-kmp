package com.example.uberapp_tim9.unregistered_user.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.DriverMainActivity;

public class RegisterSecondActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second);
        Button btn = findViewById(R.id.left_button);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterFirstActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);

            startActivity(intent);
        });

        Button register = findViewById(R.id.sign_up_button);
        register.setOnClickListener(v -> {
            Intent intent = new Intent(this, DriverMainActivity.class);

            startActivity(intent);
        });
    }

}