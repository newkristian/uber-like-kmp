package com.example.uberapp_tim9.ride_history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.ride_history.adapters.RidesAdapter;

public class RideHistoryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history, container, false);
        RidesAdapter adapter = new RidesAdapter();
        RecyclerView list = view.findViewById(R.id.rides_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        list.setLayoutManager(llm);
        list.setAdapter(adapter);


        return view;

    }


}