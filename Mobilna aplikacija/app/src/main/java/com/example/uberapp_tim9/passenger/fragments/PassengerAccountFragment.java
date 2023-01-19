package com.example.uberapp_tim9.passenger.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.model.dtos.PassengerWithoutIdPasswordDTO;
import com.example.uberapp_tim9.passenger.PassengerReportActivity;
import com.example.uberapp_tim9.passenger.favorite_rides.PassengerFavoriteRidesActivity;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.gms.common.util.Strings;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private static Passenger passenger = new Passenger(0, "Ivan", "Ivanovic", "", "0623339998", "email@mail.com", "Resavska 23", "123456789", false);

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

        Call<ResponseBody> call = RestApiManager.restApiInterfacePassenger.getPassenger(1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        PassengerWithoutIdPasswordDTO passwordDTO = new Gson().fromJson(response.body().string(), new TypeToken<PassengerWithoutIdPasswordDTO>(){}.getType());
                        passenger = new Passenger(passwordDTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loadCurrentValues(view);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });

        Button changeButton = view.findViewById(R.id.changeButton);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button favoriteRidesButton = view.findViewById(R.id.favoriteRidesButton);
        Button reportButton = view.findViewById(R.id.reportsButton);

        changeButton.setOnClickListener(view1 -> {
            changeFormEnabled(view, true);
            buttonsEditMode(changeButton, confirmButton, cancelButton);
        });

        cancelButton.setOnClickListener(view1 -> {
            changeFormEnabled(view, false);
            resetButtons(changeButton, confirmButton, cancelButton);

            loadCurrentValues(view);
        });

        confirmButton.setOnClickListener(view1 -> {
            changeFormEnabled(view, false);
            resetButtons(changeButton, confirmButton, cancelButton);
            passenger.setName(String.valueOf(((TextInputEditText) view.findViewById(R.id.first_name_text_input_edit_text)).getText()));
            passenger.setSurname(String.valueOf(((TextInputEditText) view.findViewById(R.id.last_name_text_input_edit_text)).getText()));
            passenger.setTelephoneNumber(String.valueOf(((TextInputEditText) view.findViewById(R.id.phone_number_text_input_edit_text)).getText()));
            passenger.setAddress(String.valueOf(((TextInputEditText) view.findViewById(R.id.address_text_input_edit_text)).getText()));
            passenger.setEmail(String.valueOf(((TextInputEditText) view.findViewById(R.id.email_text_input_edit_text)).getText()));
        });

        favoriteRidesButton.setOnClickListener(v -> v.getContext().startActivity(new Intent(v.getContext(), PassengerFavoriteRidesActivity.class)));

        reportButton.setOnClickListener(v -> v.getContext().startActivity(new Intent(v.getContext(), PassengerReportActivity.class)));
    }

    private void buttonsEditMode(Button changeButton, Button confirmButton, Button cancelButton) {
        changeButton.setVisibility(View.GONE);
        confirmButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    private void resetButtons(Button changeButton, Button confirmButton, Button cancelButton) {
        changeButton.setVisibility(View.VISIBLE);
        confirmButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
    }

    private void changeFormEnabled(@NonNull View view, boolean enabled) {
        view.findViewById(R.id.first_name_text_input_edit_text).setEnabled(enabled);
        view.findViewById(R.id.last_name_text_input_edit_text).setEnabled(enabled);
        view.findViewById(R.id.phone_number_text_input_edit_text).setEnabled(enabled);
        view.findViewById(R.id.email_text_input_edit_text).setEnabled(enabled);
        view.findViewById(R.id.address_text_input_edit_text).setEnabled(enabled);
    }

    private void loadCurrentValues(@NonNull View view) {
        ((TextInputEditText) view.findViewById(R.id.first_name_text_input_edit_text)).setText(passenger.getName());
        ((TextInputEditText) view.findViewById(R.id.last_name_text_input_edit_text)).setText(passenger.getSurname());
        ((TextInputEditText) view.findViewById(R.id.phone_number_text_input_edit_text)).setText(passenger.getTelephoneNumber());
        ((TextInputEditText) view.findViewById(R.id.email_text_input_edit_text)).setText(passenger.getEmail());
        ((TextInputEditText) view.findViewById(R.id.address_text_input_edit_text)).setText(passenger.getAddress());
        if (Strings.isEmptyOrWhitespace(passenger.getProfilePicture())) {
            ((ShapeableImageView) view.findViewById(R.id.profile_picture_image_view)).setImageResource(R.drawable.ic_profile);
        } else {
            byte[] decodedProfilePicture = Base64.decode(passenger.getProfilePicture(), Base64.DEFAULT);
            Bitmap profilePicture = BitmapFactory.decodeByteArray(decodedProfilePicture, 0, decodedProfilePicture.length);
            ((ShapeableImageView) view.findViewById(R.id.profile_picture_image_view)).setImageBitmap(profilePicture);
        }
    }

    @NonNull
    @Override
    public LayoutInflater onGetLayoutInflater(Bundle savedInstanceState) {
        LayoutInflater inflater = super.onGetLayoutInflater(savedInstanceState);
        Context contextThemeWrapper = new ContextThemeWrapper(requireContext(), R.style.Theme_UberApp_Tim9_Basic);
        return inflater.cloneInContext(contextThemeWrapper);
    }
}