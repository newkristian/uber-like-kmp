package com.example.uberapp_tim9.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.notificationManager.NotificationService;
import com.example.uberapp_tim9.driver.rest.RestApiManager;
import com.example.uberapp_tim9.model.dtos.RejectionReasonDTO;
import com.example.uberapp_tim9.passenger.PassengerMainActivity;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideRejectionActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_rejection);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Button sendRejectionReason = findViewById(R.id.reject_button);
        sendRejectionReason.setOnClickListener(view ->{
                        TextInputEditText reason = findViewById(R.id.rejection_reason);
                        String reasonText = reason.getText().toString().trim();
                        if(reasonText.length() == 0) {
                            reason.setError(getString(R.string.zeroLengthError));
                        }
                        else {
                            String ride_id = getIntent().getStringExtra("ride_id");
                            RejectionReasonDTO reasonDTO = new RejectionReasonDTO(reasonText);
                            Call<ResponseBody> call = RestApiManager.restApiInterface.denyRide(ride_id,reasonDTO);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.code() == 200){
                                        Toast.makeText(getApplicationContext(), "Vožnja uspešno odbijena!", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (response.code() == 400){
                                        Toast.makeText(getApplicationContext(), "Ne možete odbiti vožnju koja nema status 'Kreirana'!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Vožnja ne postoji!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                                }
                            });
                            notificationManager.cancel(NotificationService.NOTIFICATION_ID);
                            NotificationService.NOTIFICATION_ID += 1;
                            this.finish();
                        }
        });
    }
}