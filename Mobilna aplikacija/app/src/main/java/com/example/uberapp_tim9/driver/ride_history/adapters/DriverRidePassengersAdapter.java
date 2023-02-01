package com.example.uberapp_tim9.driver.ride_history.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.driver.ride_history.DriverRideHistoryMockupData;

import java.util.List;

public class DriverRidePassengersAdapter extends RecyclerView.Adapter<DriverRidePassengersAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameSurnameTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            mNameSurnameTextView = (TextView) itemView.findViewById(R.id.sidenav_full_name);
        }

        public TextView getmNameSurnameTextView() {
            return mNameSurnameTextView;
        }
    }

    @NonNull
    @Override
    public DriverRidePassengersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passenger_list_item, parent, false);
        return new DriverRidePassengersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverRidePassengersAdapter.ViewHolder holder, int position) {
        List<Passenger> passengers = DriverRideHistoryMockupData.getPassengers();
        holder.getmNameSurnameTextView().setText(passengers.get(position).getName() + " " + passengers.get(position).getSurname());
    }


    @Override
    public int getItemCount() {
        return DriverRideHistoryMockupData.getPassengers().size();
    }


}
