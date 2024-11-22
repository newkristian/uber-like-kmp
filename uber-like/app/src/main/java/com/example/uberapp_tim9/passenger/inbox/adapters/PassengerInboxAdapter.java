package com.example.uberapp_tim9.passenger.inbox.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.ride_history.DriverRideDetailsActivity;
import com.example.uberapp_tim9.driver.ride_history.DriverRideHistoryMockupData;
import com.example.uberapp_tim9.model.Message;
import com.example.uberapp_tim9.model.MessageType;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.passenger.inbox.InboxMessagesMockupData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PassengerInboxAdapter extends RecyclerView.Adapter<PassengerInboxAdapter.ViewHolder> {

    private Context context;

    public PassengerInboxAdapter(Context applicationContext) {
        this.context = applicationContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        //public TextView pathTextView;
        private TextView mFromTextView;
        private TextView mMessageTextView;
        private TextView mTimeTextView;
        private FloatingActionButton mInfoButton;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            //pathTextView = (TextView) itemView.findViewById(R.id.path);
            mTimeTextView = (TextView) itemView.findViewById(R.id.inbox_message_time);
            mFromTextView = (TextView) itemView.findViewById(R.id.from_box);
            mMessageTextView = (TextView) itemView.findViewById(R.id.inbox_message);
            mInfoButton = (FloatingActionButton) itemView.findViewById(R.id.infoButtonInbox);
        }

        public TextView getmFromTextView() {
            return mFromTextView;
        }

        public TextView getmMessageTextView() {
            return mMessageTextView;
        }

        public TextView getmTimeTextView(){
            return mTimeTextView;
        }

        public FloatingActionButton getmInfoButton() { return mInfoButton; }
    }


    @NonNull
    @Override
    public PassengerInboxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_list_item, parent, false);
        return new PassengerInboxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerInboxAdapter.ViewHolder holder, int position) {
        List<Message> messages = InboxMessagesMockupData.getMessages();
        if(messages.get(position).getMessageType() == MessageType.PANIC){
            holder.getmInfoButton().setBackgroundTintList(context.getResources().getColorStateList(R.color.bright_red));
        }
        if(messages.get(position).getMessageType() == MessageType.SUPPORT){
            holder.getmInfoButton().setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
        }
        holder.getmFromTextView().setText(messages.get(position).getSender().getName());
        holder.getmTimeTextView().setText(messages.get(position).getSentDateTime().toString());
        holder.getmMessageTextView().setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return InboxMessagesMockupData.getMessages().size();
    }



}

