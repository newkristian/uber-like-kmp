package com.example.uberapp_tim9.driver.ride_history.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Message;
import com.example.uberapp_tim9.model.dtos.MessageDTO;
import com.example.uberapp_tim9.passenger.adapters.MessagesListAdapter;
import com.example.uberapp_tim9.passenger.adapters.MessagesMockupData;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessagesTransferedAdapter extends RecyclerView.Adapter<MessagesTransferedAdapter.ViewHolder> {

    private final int currentUser;
    private  List<MessageDTO> messages;

    public MessagesTransferedAdapter(int currentUser,List<MessageDTO> messages) {
        this.currentUser = currentUser;
        this.messages = messages;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView getTimeSent() {
            return timeSent;
        }

        public void setTimeSent(TextView timeSent) {
            this.timeSent = timeSent;
        }

        public TextView getMessage() {
            return message;
        }

        public void setMessage(TextView message) {
            this.message = message;
        }

        private TextView timeSent;
        private TextView message;

        public ViewHolder(View itemView) {
            super(itemView);
            timeSent = itemView.findViewById(R.id.time_sent);
            message = itemView.findViewById(R.id.sidenav_full_name);
        }
    }

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public List<Message> getmMessageList() {
        return mMessageList;
    }

    private List<Message> mMessageList = MessagesMockupData.messages;

    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);

        if (message.getSender().getId() == currentUser) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public MessagesTransferedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_item_me, parent, false);
        }
        else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_item_other, parent, false);
        }
        return new MessagesTransferedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesTransferedAdapter.ViewHolder holder, int position) {
        holder.getMessage().setText(messages.get(position).getMessage());
        holder.getTimeSent().setText(messages.get(position).getSentDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
