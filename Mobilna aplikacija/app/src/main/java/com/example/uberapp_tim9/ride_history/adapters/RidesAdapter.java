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
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.ride_history.DriverRideHistoryMockupData;

import org.w3c.dom.Text;

import java.util.List;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        //public TextView pathTextView;
        public TextView startTimeTextView;
        public TextView endTimeTextView;
        public TextView distanceTextView;
        public TextView timeTotalTextView;
        public TextView priceTotalTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            //pathTextView = (TextView) itemView.findViewById(R.id.path);
            startTimeTextView = (TextView) itemView.findViewById(R.id.start_time);
            endTimeTextView = (TextView) itemView.findViewById(R.id.end_time);
            distanceTextView = (TextView) itemView.findViewById(R.id.distance);
            timeTotalTextView = (TextView) itemView.findViewById(R.id.timeTotal);
            priceTotalTextView = (TextView) itemView.findViewById(R.id.priceTotal);
        }

       /* public TextView getPathTextView() {
            return pathTextView;
        }*/

        public TextView getStartTimeTextView() {
            return startTimeTextView;
        }

        public TextView getEndTimeTextView() {
            return endTimeTextView;
        }

        public TextView getDistanceTextView() {
            return distanceTextView;
        }

        public TextView getTimeTotalTextView() {
            return timeTotalTextView;
        }

        public TextView getPriceTotalTextView() {
            return priceTotalTextView;
        }
    }

    @NonNull
    @Override
    public RidesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rides_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RidesAdapter.ViewHolder holder, int position) {
        List<Ride> rides = DriverRideHistoryMockupData.getRides();
        holder.getStartTimeTextView().setText(rides.get(position).getmStartTime().toString());
        holder.getEndTimeTextView().setText(rides.get(position).getmEndTime().toString());
        holder.getDistanceTextView().setText(Double.toString(rides.get(position).getTotalKilometers()) + " km");
        holder.getTimeTotalTextView().setText(rides.get(position).getmEstimatedTime().toString());
        holder.getPriceTotalTextView().setText(Double.toString(rides.get(position).getmTotalPrice()) + " din");
    }

    @Override
    public int getItemCount() {
        return DriverRideHistoryMockupData.getRides().size();
    }
}
