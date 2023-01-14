package com.example.uberapp_tim9.driver.inbox;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Message;
import com.example.uberapp_tim9.model.MessageType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DriverInboxAdapter extends RecyclerView.Adapter<DriverInboxAdapter.ViewHolder> {

    private Context context;

    public DriverInboxAdapter(Context applicationContext) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Message> messages = DriverInboxMessagesMockupData.getMessages();
        if(messages.get(position).getmMessageType() == MessageType.PANIC){
            holder.getmInfoButton().setBackgroundTintList(context.getResources().getColorStateList(R.color.bright_red));
        }
        if(messages.get(position).getmMessageType() == MessageType.SUPPORT){
            holder.getmInfoButton().setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
        }
        holder.getmFromTextView().setText(messages.get(position).getmSender().getmName());
        holder.getmTimeTextView().setText(messages.get(position).getmTimeSent().toString());
        holder.getmMessageTextView().setText(messages.get(position).getmMessage());
    }

    @Override
    public int getItemCount() {
        return DriverInboxMessagesMockupData.getMessages().size();
    }
}
