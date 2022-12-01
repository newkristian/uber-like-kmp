package com.example.uberapp_tim9.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.main_page.DriverMainActivity;

public class PassengerRegisterSecondActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second);
        Button btn = findViewById(R.id.left_button);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PassengerRegisterActivity.class);
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