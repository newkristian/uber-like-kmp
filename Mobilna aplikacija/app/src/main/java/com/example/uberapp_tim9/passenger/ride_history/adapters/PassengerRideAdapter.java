package com.example.uberapp_tim9.passenger.ride_history.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideDetailsActivity;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideHistoryMockupData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class PassengerRideAdapter extends RecyclerView.Adapter<PassengerRideAdapter.ViewHolder> {
    Ride ride;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        //public TextView pathTextView;
        private TextView mStartTimeTextView;
        private TextView mEndTimeTextView;
        private TextView mDistanceTextView;
        private TextView mPassengersTotalTextView;
        private TextView mPriceTotalTextView;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            //pathTextView = (TextView) itemView.findViewById(R.id.path);
            mStartTimeTextView = (TextView) itemView.findViewById(R.id.start_time);
            mEndTimeTextView = (TextView) itemView.findViewById(R.id.end_time);
            mDistanceTextView = (TextView) itemView.findViewById(R.id.distance);
            mPassengersTotalTextView = (TextView) itemView.findViewById(R.id.passengersTotal);
            mPriceTotalTextView = (TextView) itemView.findViewById(R.id.priceTotal);
        }


        public TextView getmStartTimeTextView() {
            return mStartTimeTextView;
        }

        public TextView getmEndTimeTextView() {
            return mEndTimeTextView;
        }

        public TextView getmDistanceTextView() {
            return mDistanceTextView;
        }

        public TextView getmPassengersTotalTextView() {
            return mPassengersTotalTextView;
        }

        public TextView getmPriceTotalTextView() {
            return mPriceTotalTextView;
        }
    }

    public PassengerRideAdapter(Ride ride) {
        this.ride = ride;
    }

    @NonNull
    @Override
    public PassengerRideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passenger_ride_list_item, parent, false);
        return new PassengerRideAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerRideAdapter.ViewHolder holder, int position) {
        holder.getmStartTimeTextView().setText(ride.getmStartTime().toString());
        holder.getmEndTimeTextView().setText(ride.getmEndTime().toString());
        holder.getmDistanceTextView().setText(String.format(Locale.getDefault(), "%.2f km", ride.getTotalKilometers()));
        holder.getmPassengersTotalTextView().setText(Integer.toString(ride.getTotalPassengers()));
        holder.getmPriceTotalTextView().setText(ride.getmTotalPrice() + " din");
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
