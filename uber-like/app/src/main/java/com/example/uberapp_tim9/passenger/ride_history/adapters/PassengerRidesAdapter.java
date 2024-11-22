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
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.passenger.PassengerMainActivity;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideDetailsActivity;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideHistoryMockupData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PassengerRidesAdapter extends RecyclerView.Adapter<PassengerRidesAdapter.ViewHolder> {
    private final List<Ride> rides;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        //public TextView pathTextView;
        private TextView mStartTimeTextView;
        private TextView mEndTimeTextView;
        private TextView mDistanceTextView;
        private TextView mPassengersTotalTextView;
        private TextView mPriceTotalTextView;
        private FloatingActionButton mInfoButton;


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
            mInfoButton = (FloatingActionButton) itemView.findViewById(R.id.infoButton);
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

        public FloatingActionButton getmInfoButton() { return mInfoButton; }
    }

    public PassengerRidesAdapter(List<Ride> rides) {
        this.rides = rides;
    }

    @NonNull
    @Override
    public PassengerRidesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rides_list_item, parent, false);
        return new PassengerRidesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerRidesAdapter.ViewHolder holder, int position) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        holder.getmStartTimeTextView().setText(rides.get(position).getmStartTime().format(dtf));
        holder.getmEndTimeTextView().setText(rides.get(position).getmEndTime().format(dtf));
        holder.getmDistanceTextView().setText(String.format(Locale.getDefault(), "%.2f km", rides.get(position).getTotalKilometers()));
        holder.getmPassengersTotalTextView().setText(Integer.toString(rides.get(position).getTotalPassengers()));
        holder.getmPriceTotalTextView().setText(String.format("%.2f din", rides.get(position).getmTotalPrice()));

        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.startActivity(new Intent(activity.getBaseContext(), PassengerRideDetailsActivity.class)
                    .putExtra("ride", PassengerMainActivity.gson.toJson(rides.get(position))));
        });
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }
}
