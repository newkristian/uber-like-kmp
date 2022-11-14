package com.example.uberapp_tim9.ride_history.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.ride_history.DriverRideHistoryMockupData;

public class RidesAdapter extends BaseAdapter {

    private Activity mParentActivity;

    public RidesAdapter(Activity mParentActivity) {
        this.mParentActivity = mParentActivity;
    }

    @Override
    public int getCount() {
        return DriverRideHistoryMockupData.getRides().size();
    }

    @Override
    public Object getItem(int i) {
        return DriverRideHistoryMockupData.getRides().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Ride ride = DriverRideHistoryMockupData.getRides().get(i);
        View listItem = view;
        if (view == null) {
            listItem = mParentActivity.getLayoutInflater().inflate(R.layout.rides_list_item, null);
        }
        ((TextView) listItem.findViewById(R.id.start_time_input)).setText(ride.getmStartTime().toString());
        ((TextView) listItem.findViewById(R.id.end_time_input)).setText(ride.getmEndTime().toString());
        ((TextView) listItem.findViewById(R.id.rating)).setText(Integer.toString(ride.getmPassengers().size()));

        return listItem;
    }
}
