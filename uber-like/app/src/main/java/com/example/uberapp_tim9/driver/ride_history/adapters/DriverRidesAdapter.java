package com.example.uberapp_tim9.driver.ride_history.adapters;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.driver.ride_history.DriverRideDetailsActivity;
import com.example.uberapp_tim9.driver.ride_history.DriverRideHistoryMockupData;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.RidePageDTO;
import com.example.uberapp_tim9.model.dtos.RouteDTO;
import com.example.uberapp_tim9.model.dtos.VehicleDTO;
import com.example.uberapp_tim9.passenger.EstimatesService;
import com.example.uberapp_tim9.passenger.PassengerMainActivity;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRidesAdapter extends RecyclerView.Adapter<DriverRidesAdapter.ViewHolder> {

    private List<RideCreatedDTO> rides = new ArrayList<>();
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

    public DriverRidesAdapter(List<RideCreatedDTO> returnedRides) {
        rides = returnedRides;
    }


    @NonNull
    @Override
    public DriverRidesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rides_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverRidesAdapter.ViewHolder holder, int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        holder.getmStartTimeTextView().setText(formatter.format(rides.get(position).getStartTime()));
        holder.getmEndTimeTextView().setText(formatter.format(rides.get(position).getEndTime()));
        for(RouteDTO route : rides.get(position).getLocations()){
            double distance = EstimatesService.calculateDistance(route.getDeparture(),route.getDestination());
            holder.getmDistanceTextView().setText(String.format("%.2f", distance) + " km");
            break;
        }

        holder.getmPassengersTotalTextView().setText(Integer.toString(rides.get(position).getPassengers().size()));
        holder.getmPriceTotalTextView().setText(String.format("%.2f", rides.get(position).getTotalCost()) + " din");
        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.startActivity(new Intent(activity.getBaseContext(), DriverRideDetailsActivity.class).putExtra("ride", PassengerMainActivity.gson.toJson(rides.get(position))));
        });

    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

}
