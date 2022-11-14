package com.example.uberapp_tim9.ride_history.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.ride_history.DriverRideHistoryMockupData;

public class RidePassengersAdapter extends BaseAdapter {
    private Activity mParentActivity;

    public RidePassengersAdapter(Activity mParentActivity) {
        this.mParentActivity = mParentActivity;
    }

    @Override
    public int getCount() {
        return DriverRideHistoryMockupData.getPassengers().size();
    }

    @Override
    public Object getItem(int i) {
        return DriverRideHistoryMockupData.getPassengers().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Passenger passenger = DriverRideHistoryMockupData.getPassengers().get(i);
        View listItem = view;
        if (view == null) {
            listItem = mParentActivity.getLayoutInflater().inflate(R.layout.passenger_list_item, null);
        }
        ((TextView) listItem.findViewById(R.id.name_surname)).setText(passenger.getmName() + " " + passenger.getmSurname());

        return listItem;
    }
}
