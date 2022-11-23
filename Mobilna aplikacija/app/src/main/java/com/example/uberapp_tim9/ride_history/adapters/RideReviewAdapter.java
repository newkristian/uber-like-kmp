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
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.model.Review;
import com.example.uberapp_tim9.ride_history.DriverRideHistoryMockupData;

import java.util.List;

public class RideReviewAdapter extends RecyclerView.Adapter<RideReviewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mMarkTextView;
        private TextView mCommentTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            mMarkTextView = (TextView) itemView.findViewById(R.id.mark);
            mCommentTextView = (TextView) itemView.findViewById(R.id.comment);
        }

        public TextView getmMarkTextView() {
            return mMarkTextView;
        }
        public TextView getmCommentTextView() {
            return mCommentTextView;
        }
    }

    @NonNull
    @Override
    public RideReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_list_item, parent, false);
        return new RideReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideReviewAdapter.ViewHolder holder, int position) {
        List<Review> reviews = DriverRideHistoryMockupData.getRideReviews();
        holder.getmMarkTextView().setText(Integer.toString(reviews.get(position).getmRating()));
        holder.getmCommentTextView().setText(reviews.get(position).getmComment());
    }


    @Override
    public int getItemCount() {
        return DriverRideHistoryMockupData.getRideReviews().size();
    }


}
