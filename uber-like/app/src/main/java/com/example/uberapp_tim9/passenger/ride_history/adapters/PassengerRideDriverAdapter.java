package com.example.uberapp_tim9.passenger.ride_history.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Driver;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.model.dtos.DriverDTO;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideHistoryMockupData;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRideDriverAdapter extends RecyclerView.Adapter<PassengerRideDriverAdapter.ViewHolder>{
    Ride ride;

    public PassengerRideDriverAdapter(Ride ride) {
        this.ride = ride;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameSurnameTextView;
        private ShapeableImageView mProfilePictureImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameSurnameTextView = (TextView) itemView.findViewById(R.id.sidenav_full_name);
            mProfilePictureImageView = (ShapeableImageView) itemView.findViewById(R.id.profilePictureImageView);
        }

        public TextView getmNameSurnameTextView() {
            return mNameSurnameTextView;
        }

        public ShapeableImageView getmProfilePictureImageView() {
            return mProfilePictureImageView;
        }
    }

    @NonNull
    @Override
    public PassengerRideDriverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passenger_list_item, parent, false);
        return new PassengerRideDriverAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerRideDriverAdapter.ViewHolder holder, int position) {
        Driver driver = ride.getmDriver();

        Call<ResponseBody> call = RestApiManager.restApiInterfaceDriver.getDriver(driver.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        DriverDTO driverDTO = new Gson().fromJson(response.body().string(),
                                new TypeToken<DriverDTO>(){}.getType());
                        holder.getmNameSurnameTextView().setText(driverDTO.getName() + " " + driverDTO.getSurname());
                        byte[] decodedString = Base64.decode(driverDTO.getProfilePicture(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holder.getmProfilePictureImageView().setImageBitmap(decodedByte);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

        holder.getmNameSurnameTextView().setText(String.format("%s %s", driver.getName(), driver.getSurname()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
