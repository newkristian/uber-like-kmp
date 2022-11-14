package com.example.uberapp_tim9.ride_history.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.model.Review;
import com.example.uberapp_tim9.ride_history.DriverRideHistoryMockupData;

public class RideReviewAdapter extends BaseAdapter {

    private Activity mParentActivity;

    public RideReviewAdapter(Activity mParentActivity) {
        this.mParentActivity = mParentActivity;
    }

    @Override
    public int getCount() {
        return DriverRideHistoryMockupData.getRideReviews().size();
    }

    @Override
    public Object getItem(int i) {
        return DriverRideHistoryMockupData.getRideReviews().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Review review = DriverRideHistoryMockupData.getRideReviews().get(i);
        View listItem = view;
        if (view == null) {
            listItem = mParentActivity.getLayoutInflater().inflate(R.layout.reviews_list_item, null);
        }
        ((TextView) listItem.findViewById(R.id.rating)).setText(Integer.toString(review.getmRating()));
        ((TextView) listItem.findViewById(R.id.comment)).setText(review.getmComment());

        return listItem;
    }
}
