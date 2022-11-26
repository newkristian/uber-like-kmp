package com.example.uberapp_tim9;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.uberapp_tim9.model.Passenger;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerAccountFragment newInstance(String param1, String param2) {
        PassengerAccountFragment fragment = new PassengerAccountFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Passenger passenger = new Passenger(0, "Ivan", "Ivanovic", "", "0623339998", "email@mail.com", "Resavska 23", "123456789", false);
        ((TextInputEditText)view.findViewById(R.id.name_text_input_edit_text)).setText(String.format("%s %s", passenger.getmName(), passenger.getmSurname()));
        ((TextInputEditText)view.findViewById(R.id.phone_number_text_input_edit_text)).setText(passenger.getmPhoneNumber());
        ((TextInputEditText)view.findViewById(R.id.email_text_input_edit_text)).setText(passenger.getmEmail());
        ((TextInputEditText)view.findViewById(R.id.address_text_input_edit_text)).setText(passenger.getmAddress());
        ((ShapeableImageView)view.findViewById(R.id.profile_picture_image_view)).setImageResource(R.drawable.ic_branislav);
    }

    @NonNull
    @Override
    public LayoutInflater onGetLayoutInflater(Bundle savedInstanceState) {
        LayoutInflater inflater = super.onGetLayoutInflater(savedInstanceState);
        Context contextThemeWrapper = new ContextThemeWrapper(requireContext(), R.style.Theme_UberApp_Tim9_Basic);
        return inflater.cloneInContext(contextThemeWrapper);
    }
}