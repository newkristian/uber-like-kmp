package com.example.uberapp_tim9.ride_history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.ride_history.adapters.RidesAdapter;

public class RideHistoryFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new RidesAdapter(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ride_history, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //Prelaz na aktivnost za prikazivanje ostalih podataka o voznji
        Intent intent = new Intent(getActivity(), RideDetailsActivity.class);
        startActivity(intent);
    }
}