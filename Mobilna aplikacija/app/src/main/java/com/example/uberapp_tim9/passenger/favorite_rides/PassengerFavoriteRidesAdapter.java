package com.example.uberapp_tim9.passenger.favorite_rides;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Path;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideDetailsActivity;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideHistoryMockupData;
import com.example.uberapp_tim9.passenger.ride_history.adapters.PassengerRidesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PassengerFavoriteRidesAdapter extends RecyclerView.Adapter<PassengerFavoriteRidesAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mStartLocationTextView;
        private final TextView mEndLocationTextView;
        private final ImageView mMapImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mStartLocationTextView = itemView.findViewById(R.id.fromTextView);
            mEndLocationTextView = itemView.findViewById(R.id.toTextView);
            mMapImageView = itemView.findViewById(R.id.map);
        }

        public TextView getmStartLocationTextView() {
            return mStartLocationTextView;
        }

        public TextView getmEndLocationTextView() {
            return mEndLocationTextView;
        }

        public ImageView getmMapImageView() {
            return mMapImageView;
        }
    }

    @NonNull
    @Override
    public PassengerFavoriteRidesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_routes_list_item, parent, false);
        return new PassengerFavoriteRidesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerFavoriteRidesAdapter.ViewHolder holder, int position) {
        List<Path> routes = PassengerFavoriteRoutesMockupData.getPaths();
        holder.getmStartLocationTextView().setText(String.format("Od: %s", routes.get(position).getmStartPoint().getmAddress()));
        holder.getmEndLocationTextView().setText(String.format("Do: %s", routes.get(position).getmEndPoint().getmAddress()));
        holder.getmMapImageView().setImageResource(R.drawable.maps);
    }

    @Override
    public int getItemCount() {
        return PassengerFavoriteRoutesMockupData.getPaths().size();
    }
}
