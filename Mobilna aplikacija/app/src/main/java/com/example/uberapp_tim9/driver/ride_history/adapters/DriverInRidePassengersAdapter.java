package com.example.uberapp_tim9.driver.ride_history.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.ride_history.DriverInRidePassengersData;
import com.example.uberapp_tim9.model.dtos.PassengerWithoutIdPasswordDTO;

import java.util.List;

public class DriverInRidePassengersAdapter extends RecyclerView.Adapter<DriverInRidePassengersAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameSurnameTextView;
        private ImageView mCallImageView;
        private ImageView mMessageImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameSurnameTextView = (TextView) itemView.findViewById(R.id.message_bubble);
            mCallImageView = (ImageView) itemView.findViewById(R.id.callImageView);
            mMessageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
        }

        public TextView getmNameSurnameTextView() {
            return mNameSurnameTextView;
        }

        public ImageView getmCallImageView() {
            return mCallImageView;
        }

        public ImageView getmMessageImageView() {
            return mMessageImageView;
        }
    }

    @NonNull
    @Override
    public DriverInRidePassengersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passenger_in_ride_list_item, parent, false);
        return new DriverInRidePassengersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverInRidePassengersAdapter.ViewHolder holder, int position) {
        List<PassengerWithoutIdPasswordDTO> passengers = DriverInRidePassengersData.passengers;
        holder.getmNameSurnameTextView().setText(passengers.get(position).name + " " + passengers.get(position).surname);

        holder.getmMessageImageView().setOnClickListener(v -> {
            Intent messageIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + passengers.get(holder.getAdapterPosition()).telephoneNumber));
            messageIntent.putExtra("sms_body", "");
            v.getContext().startActivity(messageIntent);
        });

        holder.getmCallImageView().setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + passengers.get(holder.getAdapterPosition()).telephoneNumber));
            v.getContext().startActivity(callIntent);
        });
    }


    @Override
    public int getItemCount() {
        return DriverInRidePassengersData.passengers.size();
    }


}
