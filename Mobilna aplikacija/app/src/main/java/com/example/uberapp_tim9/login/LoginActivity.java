package com.example.uberapp_tim9.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.main_page.PassengerMainActivity;
import com.example.uberapp_tim9.registration.PassengerRegisterActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), PassengerMainActivity.class)));

        Button register = findViewById(R.id.sign_up_button);
        register.setOnClickListener(view ->
        {startActivity(new Intent(getApplicationContext(), PassengerRegisterActivity.class));
                this.finish();});
    }

}