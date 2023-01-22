package com.example.uberapp_tim9.passenger.favorite_rides;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Path;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.model.dtos.FavoritePathDTO;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideDetailsActivity;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideHistoryMockupData;
import com.example.uberapp_tim9.passenger.ride_history.adapters.PassengerRidesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PassengerFavoriteRidesAdapter extends RecyclerView.Adapter<PassengerFavoriteRidesAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mFavoriteName;
        private final TextView mStartLocationTextView;
        private final TextView mEndLocationTextView;
        private final TextView mVehicleTypeTextView;
        private final LinearLayout mBabyTransportLinearLayout;
        private final LinearLayout mPetTransportLinearLayout;
        private final TextView mBabyTransportTextView;
        private final TextView mPetTransportTextView;
        private final ImageView mMapImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mFavoriteName = itemView.findViewById(R.id.nameTextView);
            mStartLocationTextView = itemView.findViewById(R.id.fromTextView);
            mEndLocationTextView = itemView.findViewById(R.id.toTextView);
            mVehicleTypeTextView = itemView.findViewById(R.id.vehicleTypeTextView);
            mBabyTransportLinearLayout = itemView.findViewById(R.id.babyTransportLinearLayout);
            mPetTransportLinearLayout = itemView.findViewById(R.id.petTransportLinearLayout);
            mBabyTransportTextView = itemView.findViewById(R.id.babyTransportTextView);
            mPetTransportTextView = itemView.findViewById(R.id.petTransportTextView);
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
        List<FavoritePathDTO> routes = PassengerFavoriteRoutesMockupData.getPaths();

        FavoritePathDTO route = routes.get(position);
        holder.mFavoriteName.setText(String.format("Naziv: %s", route.getFavoriteName()));
        holder.getmStartLocationTextView().setText(String.format("Od: %s", route.getLocations().get("departure").getmAddress()));
        holder.getmEndLocationTextView().setText(String.format("Do: %s", route.getLocations().get("destination").getmAddress()));
        holder.mVehicleTypeTextView.setText(String.format("Tip vozila: %s", route.getVehicleType()));
        if (route.isBabyTransport()) {
            holder.mBabyTransportLinearLayout.setVisibility(View.VISIBLE);
        }
        if (route.isPetTransport()) {
            holder.mPetTransportLinearLayout.setVisibility(View.VISIBLE);
        }
        holder.getmMapImageView().setImageResource(R.drawable.maps);
    }

    @Override
    public int getItemCount() {
        return PassengerFavoriteRoutesMockupData.getPaths().size();
    }
}
