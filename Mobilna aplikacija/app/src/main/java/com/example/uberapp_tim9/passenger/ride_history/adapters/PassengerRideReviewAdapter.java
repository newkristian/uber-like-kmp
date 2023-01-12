package com.example.uberapp_tim9.passenger.ride_history.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Review;
import com.example.uberapp_tim9.passenger.ride_history.PassengerRideHistoryMockupData;

import java.util.List;

public class PassengerRideReviewAdapter extends RecyclerView.Adapter<PassengerRideReviewAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mMarkTextView;
        private TextView mCommentTextView;
        private TextView mReviewedTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mMarkTextView = itemView.findViewById(R.id.mark);
            mCommentTextView = itemView.findViewById(R.id.comment);
            mReviewedTextView = itemView.findViewById(R.id.reviewedTextView);
        }

        public TextView getmMarkTextView() {
            return mMarkTextView;
        }
        public TextView getmCommentTextView() {
            return mCommentTextView;
        }
        public TextView getmReviewedTextView() {
            return mReviewedTextView;
        }
    }

    @NonNull
    @Override
    public PassengerRideReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passenger_reviews_list_item, parent, false);
        return new PassengerRideReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerRideReviewAdapter.ViewHolder holder, int position) {
        List<Review> reviews = PassengerRideHistoryMockupData.getRideReviews();
        holder.getmMarkTextView().setText(Integer.toString(reviews.get(position).getmRating()));
        holder.getmCommentTextView().setText(reviews.get(position).getmComment());
        if (reviews.get(position).isDriverReview()) {
            holder.getmReviewedTextView().setText("Vozač");
        } else {
            holder.getmReviewedTextView().setText("Vozilo");
        }
    }


    @Override
    public int getItemCount() {
        return PassengerRideHistoryMockupData.getRideReviews().size();
    }

}
