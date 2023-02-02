package com.example.uberapp_tim9.unregistered_user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.model.dtos.UserLoginDTO;
import com.example.uberapp_tim9.passenger.PassengerMainActivity;
import com.example.uberapp_tim9.model.LoggedUserCredentials;
import com.example.uberapp_tim9.shared.LoggedUserInfo;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.example.uberapp_tim9.unregistered_user.registration.RegisterFirstActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText email;
    TextInputEditText password;

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString());
        }
    }).create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button login = findViewById(R.id.login_button);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);

        login.setOnClickListener(view -> login());

        Button register = findViewById(R.id.sign_up_button);
        register.setOnClickListener(view ->
        {startActivity(new Intent(getApplicationContext(), RegisterFirstActivity.class));
                });

        TextView reset_password_link = findViewById(R.id.reset_password_link);
        reset_password_link.setOnClickListener(view ->
        {startActivity(new Intent(getApplicationContext(), PasswordResetActivity.class));
        });
    }

    private void login() {
        String email_input = email.getText().toString().trim();
        String password_input = password.getText().toString().trim();

        if(email_input.length() == 0) {
            email.setError(getString(R.string.zeroLengthError));
            return;
        }
        if(password_input.length() == 0) {
            password.setError(getString(R.string.zeroLengthError));
            return;
        }
        UserLoginDTO loginDTO = new UserLoginDTO(email_input,password_input);
        Call<ResponseBody> login = RestApiManager.restApiInterfaceShared.login(loginDTO);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    try {
                        LoggedUserCredentials user = gson.fromJson(response.body().string(), new TypeToken<LoggedUserCredentials>(){}.getType());
                        if(user == null) {
                            Toast.makeText(getApplicationContext(), "Pogrešan email ili lozinka!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LoggedUserInfo.cloneUserCredentials(user);
                        if(user.getRole().equals("ROLE_PASSENGER")) {
                            startActivity(new Intent(getApplicationContext(), PassengerMainActivity.class));
                        }
                        else if(user.getRole().equals("ROLE_DRIVER")) {
                            startActivity(new Intent(getApplicationContext(), DriverMainActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Aplikaciju mogu koristiti samo vozač i putnik.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Pogrešan email ili lozinka!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Pogrešan email ili lozinka!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }
}