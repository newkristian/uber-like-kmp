package com.example.uberapp_tim9.driver.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Passenger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverAccountFragment extends Fragment {

    public static DriverAccountFragment newInstance(String param1, String param2) {
        DriverAccountFragment fragment = new DriverAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Passenger passenger = new Passenger(0, "Ivana", "Ivanovicka", "", "0693339999", "emaill@mail.com", "Resavska 23", "123456789", false);
        ((EditText)view.findViewById(R.id.name_edit_text)).setText(String.format("%s %s", passenger.getmName(), passenger.getmSurname()));
        ((EditText)view.findViewById(R.id.phone_number_edit_text)).setText(passenger.getmPhoneNumber());
        ((EditText)view.findViewById(R.id.email_edit_text)).setText(passenger.getmEmail());
        ((EditText)view.findViewById(R.id.address_edit_text)).setText(passenger.getmAddress());
        ((ImageView)view.findViewById(R.id.profile_picture_image_view)).setImageResource(R.drawable.ic_simic);
        ((CheckBox)view.findViewById(R.id.blocked_checkbox)).setChecked(passenger.ismIsBlocked());
    }

    @NonNull
    @Override
    public LayoutInflater onGetLayoutInflater(Bundle savedInstanceState) {
        LayoutInflater inflater = super.onGetLayoutInflater(savedInstanceState);
        Context contextThemeWrapper = new ContextThemeWrapper(requireContext(), R.style.Theme_UberApp_Tim9_Basic);
        return inflater.cloneInContext(contextThemeWrapper);
    }
}