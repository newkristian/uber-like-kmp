package com.example.uberapp_tim9.passenger.inbox;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.ride_history.adapters.DriverRidesAdapter;
import com.example.uberapp_tim9.passenger.inbox.adapters.PassengerInboxAdapter;

public class PassengerInboxFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passenger_inbox, container, false);
        PassengerInboxAdapter adapter = new PassengerInboxAdapter(getActivity().getApplicationContext());
        RecyclerView list = view.findViewById(R.id.messages_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        list.setLayoutManager(llm);
        list.setAdapter(adapter);

        return view;

    }
}