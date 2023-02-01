package com.example.uberapp_tim9.passenger.ride_history.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideHistoryMockupData;

import java.util.List;

public class PassengerRidePassengersAdapter extends RecyclerView.Adapter<PassengerRidePassengersAdapter.ViewHolder>{
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
    public PassengerRidePassengersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passenger_list_item, parent, false);
        return new PassengerRidePassengersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerRidePassengersAdapter.ViewHolder holder, int position) {
        List<Passenger> passengers = PassengerRideHistoryMockupData.getPassengers();
        holder.getmNameSurnameTextView().setText(passengers.get(position).getName() + " " + passengers.get(position).getSurname());
    }


    @Override
    public int getItemCount() {
        return PassengerRideHistoryMockupData.getPassengers().size();
    }

}
