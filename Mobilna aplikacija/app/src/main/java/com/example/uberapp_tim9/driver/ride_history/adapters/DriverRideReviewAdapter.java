package com.example.uberapp_tim9.driver.ride_history.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.ride_history.DriverRideHistoryMockupData;
import com.example.uberapp_tim9.model.Review;

import java.util.List;

public class DriverRideReviewAdapter extends RecyclerView.Adapter<DriverRideReviewAdapter.ViewHolder> {

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
    public DriverRideReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.driver_reviews_list_item, parent, false);
        return new DriverRideReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverRideReviewAdapter.ViewHolder holder, int position) {
        List<Review> reviews = DriverRideHistoryMockupData.getRideReviews();
        holder.getmMarkTextView().setText(Integer.toString(reviews.get(position).getmRating()));
        holder.getmCommentTextView().setText(reviews.get(position).getmComment());
    }


    @Override
    public int getItemCount() {
        return DriverRideHistoryMockupData.getRideReviews().size();
    }


}
