package com.example.uberapp_tim9.ride_history.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.ride_history.DriverRideHistoryMockupData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class RidePassengersAdapter extends RecyclerView.Adapter<RidePassengersAdapter.ViewHolder> {

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
    public RidePassengersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passenger_list_item, parent, false);
        return new RidePassengersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RidePassengersAdapter.ViewHolder holder, int position) {
        List<Passenger> passengers = DriverRideHistoryMockupData.getPassengers();
        holder.getmNameSurnameTextView().setText(passengers.get(position).getmName() + " " + passengers.get(position).getmSurname());
    }


    @Override
    public int getItemCount() {
        return DriverRideHistoryMockupData.getPassengers().size();
    }


}
