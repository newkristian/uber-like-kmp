package com.example.uberapp_tim9.passenger.ride_history.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Driver;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideHistoryMockupData;

import java.util.List;

public class PassengerRideDriverAdapter extends RecyclerView.Adapter<PassengerRideDriverAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameSurnameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameSurnameTextView = (TextView) itemView.findViewById(R.id.name_surname);
        }

        public TextView getmNameSurnameTextView() {
            return mNameSurnameTextView;
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
        Driver driver = PassengerRideHistoryMockupData.getDriver();
        holder.getmNameSurnameTextView().setText(String.format("%s %s", driver.getmName(), driver.getmSurname()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

}
