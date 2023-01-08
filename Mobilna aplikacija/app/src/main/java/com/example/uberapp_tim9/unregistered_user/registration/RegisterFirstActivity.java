package com.example.uberapp_tim9.unregistered_user.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim9.R;


public class RegisterFirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first);
        Button right = findViewById(R.id.right_button);
        right.setOnClickListener(view ->{
                    Intent intent = new Intent(this, RegisterSecondActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);

                    startActivity(intent);
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}