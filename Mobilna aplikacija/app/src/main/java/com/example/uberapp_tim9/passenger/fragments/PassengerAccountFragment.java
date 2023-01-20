package com.example.uberapp_tim9.passenger.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
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
    private final Integer PASSENGER_ID = 1;
    private final int PHOTOS_REQ_CODE = 1000;
    private final int CAMERA_REQ_CODE = 1;

    private static Passenger passenger = new Passenger(0, "Ivan", "Ivanovic", "", "0623339998", "email@mail.com", "Resavska 23", "123456789", false);

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
        return new PassengerAccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passenger_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Call<ResponseBody> getPassengerCall = RestApiManager.restApiInterfacePassenger.getPassenger(PASSENGER_ID);
        getPassengerCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        PassengerWithoutIdPasswordDTO passwordDTO = new Gson().fromJson(response.body().string(), new TypeToken<PassengerWithoutIdPasswordDTO>() {
                        }.getType());
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

            view.findViewById(R.id.profile_picture_image_view).setOnClickListener(view12 -> {
                Intent iPhotos = new Intent(Intent.ACTION_PICK);
                iPhotos.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                iCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.CustomAlertDialog);
                builder.setTitle("Odaberite izvor fotografije");
                builder.setItems(new CharSequence[]{"Photos", "Camera"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            startActivityForResult(iPhotos, PHOTOS_REQ_CODE);
                            break;
                        case 1:
                            startActivityForResult(iCamera, CAMERA_REQ_CODE);
                            break;
                    }
                });
                builder.create().show();
            });
        });

        cancelButton.setOnClickListener(view1 -> {
            changeFormEnabled(view, false);
            resetButtons(changeButton, confirmButton, cancelButton);
            view.findViewById(R.id.profile_picture_image_view).setOnClickListener(null);

            loadCurrentValues(view);
        });

        confirmButton.setOnClickListener(view1 -> {
            PassengerWithoutIdPasswordDTO passengerDTO = new PassengerWithoutIdPasswordDTO(
                    String.valueOf(((TextInputEditText) view.findViewById(R.id.first_name_text_input_edit_text)).getText()),
                    String.valueOf(((TextInputEditText) view.findViewById(R.id.last_name_text_input_edit_text)).getText()),
                    passenger.getProfilePicture(),
                    String.valueOf(((TextInputEditText) view.findViewById(R.id.phone_number_text_input_edit_text)).getText()),
                    String.valueOf(((TextInputEditText) view.findViewById(R.id.email_text_input_edit_text)).getText()),
                    String.valueOf(((TextInputEditText) view.findViewById(R.id.address_text_input_edit_text)).getText())
            );

            Call<ResponseBody> updatePassengerCall = RestApiManager.restApiInterfacePassenger.updatePassenger(PASSENGER_ID, passengerDTO);
            updatePassengerCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Uspešno izmenjeni podaci.", Toast.LENGTH_SHORT).show();
                        passenger = new Passenger(passengerDTO);
                        changeFormEnabled(view, false);
                        resetButtons(changeButton, confirmButton, cancelButton);
                        view.findViewById(R.id.profile_picture_image_view).setOnClickListener(null);
                    } else {
                        if (response.errorBody() == null) {
                            Toast.makeText(getContext(), "Došlo je do greške.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String error;
                        try {
                            error = response.errorBody().string().toLowerCase();
                            if (error.contains("email")) {
                                Toast.makeText(getContext(), "Email adresa je zauzeta ili nevalidna.", Toast.LENGTH_LONG).show();
                            } else if (error.contains("file")) {
                                Toast.makeText(getContext(), "Morate odabrati validnu sliku manju od 5MB.", Toast.LENGTH_LONG).show();
                            } else if (error.contains("surname")) {
                                Toast.makeText(getContext(), "Prezime je obavezno polje i ne sme biti duže od 100 slova.", Toast.LENGTH_LONG).show();
                            } else if (error.contains("name")) {
                                Toast.makeText(getContext(), "Ime je obavezno polje i ne sme biti duže od 100 slova.", Toast.LENGTH_LONG).show();
                            } else if (error.contains("phone")) {
                                Toast.makeText(getContext(), "Broj telefona mora biti validan.", Toast.LENGTH_LONG).show();
                            } else if (error.contains("address")) {
                                Toast.makeText(getContext(), "Adresa je obavezno polje i ne sme biti duža od 100 slova.", Toast.LENGTH_LONG).show();
                            } else if (error.contains("changes")) {
                                Toast.makeText(getContext(), "Niste napravili nikakve izmene.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Došlo je do sledeće greške: " + error, Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                }
            });
        });

        favoriteRidesButton.setOnClickListener(v -> v.getContext().startActivity(new Intent(v.getContext(), PassengerFavoriteRidesActivity.class)));

        reportButton.setOnClickListener(v -> v.getContext().startActivity(new Intent(v.getContext(), PassengerReportActivity.class)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTOS_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage), null, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    String imageType = options.outMimeType;
                    int uncompressedBitmapSizeInBytes = 4 * imageHeight * imageWidth;

                    if (uncompressedBitmapSizeInBytes / 10 > 5 * 1024 * 1024) {
                        Toast.makeText(getContext(), "Slika je prevelika.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    if (imageType.contains("jpeg")) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    } else {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    }

                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    passenger.setProfilePicture(encoded);
                    ((ShapeableImageView) getView().findViewById(R.id.profile_picture_image_view)).setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAMERA_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            if (bitmap.getAllocationByteCount() / 10 > 5 * 1024 * 1024) {
                Toast.makeText(getContext(), "Slika je prevelika.", Toast.LENGTH_LONG).show();
                return;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);

            passenger.setProfilePicture(encoded);
            ((ShapeableImageView) getView().findViewById(R.id.profile_picture_image_view)).setImageBitmap(bitmap);
        }
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