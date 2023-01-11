package com.example.uberapp_tim9.driver.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.driver.notificationManager.NotificationActionReceiver;
import com.example.uberapp_tim9.driver.notificationManager.NotificationService;
import com.example.uberapp_tim9.driver.rest.RestApiInterface;
import com.example.uberapp_tim9.driver.rest.RestApiManager;
import com.example.uberapp_tim9.driver.sockets_config.SocketsConfiguration;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverMainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final SocketsConfiguration socketsConfiguration = new SocketsConfiguration();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverMainFragment newInstance(String param1, String param2) {
        DriverMainFragment fragment = new DriverMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Subscribe to events from websocket (ride is ordered)
        Disposable subscription = socketsConfiguration.stompClient.topic("/ride-ordered/get-ride").subscribe(message ->
        {
            RideCreatedDTO retrieved = new Gson().fromJson(message.getPayload(), new TypeToken<RideCreatedDTO>(){}.getType());
            NotificationActionReceiver.RIDE_ID = Integer.toString(retrieved.getId());
            NotificationService.createRideAcceptanceNotification(getActivity(),retrieved.getRideSummary(), DriverMainActivity.CHANNEL_ID);
            },
        throwable -> Log.e(TAG, throwable.getMessage()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_home, container, false);
    }
}