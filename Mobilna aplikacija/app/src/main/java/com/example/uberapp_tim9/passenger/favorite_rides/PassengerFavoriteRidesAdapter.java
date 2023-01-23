package com.example.uberapp_tim9.passenger.favorite_rides;

import static com.example.uberapp_tim9.shared.directions.FetchURL.getUrl;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.dtos.FavoritePathDTO;
import com.example.uberapp_tim9.shared.directions.FetchURL;
import com.example.uberapp_tim9.shared.directions.TaskLoadedCallBack;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerFavoriteRidesAdapter extends RecyclerView.Adapter<PassengerFavoriteRidesAdapter.ViewHolder>
implements TaskLoadedCallBack {
    AppCompatActivity activity;
    private GoogleMap[] googleMaps = null;
    private Polyline[] currentPolylines = null;
    List<FavoritePathDTO> favoriteRides;

    public PassengerFavoriteRidesAdapter(AppCompatActivity activity,
                                         List<FavoritePathDTO> favoriteRides) {
        this.activity = activity;
        this.favoriteRides = favoriteRides;
    }

    public void setFavoriteRides(List<FavoritePathDTO> favoriteRides) {
        this.favoriteRides = favoriteRides;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mFavoriteName;
        private final TextView mStartLocationTextView;
        private final TextView mEndLocationTextView;
        private final TextView mVehicleTypeTextView;
        private final LinearLayout mBabyTransportLinearLayout;
        private final LinearLayout mPetTransportLinearLayout;
        private final TextView mBabyTransportTextView;
        private final TextView mPetTransportTextView;
        private final MapView mMapView;
        private final Button mDeleteButton;

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
            mMapView = itemView.findViewById(R.id.map);
            mDeleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public TextView getmStartLocationTextView() {
            return mStartLocationTextView;
        }

        public TextView getmEndLocationTextView() {
            return mEndLocationTextView;
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
    public void onTaskDone(int position, Object... values) {
        if (currentPolylines[position] != null)
            currentPolylines[position].remove();
        currentPolylines[position] = googleMaps[position].addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerFavoriteRidesAdapter.ViewHolder holder, int position) {
        if (googleMaps == null) {
            googleMaps = new GoogleMap[getItemCount()];
        }
        if (currentPolylines == null) {
            currentPolylines = new Polyline[getItemCount()];
        }

        List<FavoritePathDTO> routes = favoriteRides;

        FavoritePathDTO route = routes.get(position);
        holder.mFavoriteName.setText(String.format("Naziv: %s", route.getFavoriteName()));
        holder.getmStartLocationTextView().setText(String.format("Od: %s", route.getLocations().get(0).getDeparture().getAddress()));
        holder.getmEndLocationTextView().setText(String.format("Do: %s", route.getLocations().get(0).getDestination().getAddress()));

        String vehicleType = route.getVehicleType();
        if (vehicleType.equalsIgnoreCase("standard")) {
            vehicleType = "Standardno";
        } else if (vehicleType.equalsIgnoreCase("van")) {
            vehicleType = "Kombi";
        } else if (vehicleType.equalsIgnoreCase("luxury")) {
            vehicleType = "Luksuzno";
        }

        holder.mVehicleTypeTextView.setText(String.format("Tip vozila: %s", vehicleType));
        if (route.isBabyTransport()) {
            holder.mBabyTransportLinearLayout.setVisibility(View.VISIBLE);
        }
        if (route.isPetTransport()) {
            holder.mPetTransportLinearLayout.setVisibility(View.VISIBLE);
        }

        holder.mMapView.onCreate(null);
        holder.mMapView.getMapAsync(googleMap -> {
            googleMaps[position] = googleMap;

            LatLng departure = new LatLng(route.getLocations().get(0).getDeparture().getLatitude(), route.getLocations().get(0).getDeparture().getLongitude());
            LatLng destination = new LatLng(route.getLocations().get(0).getDestination().getLatitude(), route.getLocations().get(0).getDestination().getLongitude());

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(departure);
            builder.include(destination);

            LatLngBounds bounds = builder.build();
            int padding = 100;

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cu);

            googleMap.addMarker(new MarkerOptions().position(departure).title("Od"));
            googleMap.addMarker(new MarkerOptions().position(destination).title("Do"));

            new FetchURL(this, position).execute(getUrl(departure, destination, "driving"), "driving");
        });

        holder.mDeleteButton.setOnClickListener(view -> {
            Call<ResponseBody> deleteRide = RestApiManager.restApiInterfacePassenger.deleteFavoriteRide(route.getId());
            deleteRide.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        favoriteRides.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, favoriteRides.size());
                        Toast.makeText(activity, "Uspešno obrisana ruta.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Greška prilikom brisanja rute.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return favoriteRides.size();
    }
}
