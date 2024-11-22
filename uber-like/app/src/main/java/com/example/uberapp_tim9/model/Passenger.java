package com.example.uberapp_tim9.model;

import androidx.annotation.Nullable;

import com.example.uberapp_tim9.model.dtos.PassengerIdEmailDTO;
import com.example.uberapp_tim9.model.dtos.PassengerWithoutIdPasswordDTO;

public class Passenger extends User {
    public Passenger(int mID,
                     String mName,
                     String mSurname,
                     String mProfilePicture,
                     String mPhoneNumber,
                     String mEmail,
                     String mAddress,
                     String mPassword,
                     boolean mIsBlocked) {
        super(mID, mName, mSurname, mProfilePicture, mPhoneNumber, mEmail, mAddress, mPassword, mIsBlocked);
    }

    public Passenger() {
        super();
    }

    public Passenger(String mIme, String mPrezime) {
        super(mIme, mPrezime);
    }

    public Passenger(String mIme, String mPrezime, String mPhoneNumber) {
        super(mIme, mPrezime, mPhoneNumber);
    }

    public Passenger(PassengerWithoutIdPasswordDTO passwordDTO) {
        super(-1, passwordDTO.getName(), passwordDTO.getSurname(), passwordDTO.getProfilePicture(), passwordDTO.getTelephoneNumber(), passwordDTO.getEmail(), passwordDTO.getAddress(), "", false);
    }

    public Passenger(PassengerIdEmailDTO passenger) {
        super(passenger.getId(), "Jovo", "Jović", null, null, passenger.getEmail(), null, null, false);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof PassengerWithoutIdPasswordDTO) {
            PassengerWithoutIdPasswordDTO passenger = (PassengerWithoutIdPasswordDTO) obj;
            return passenger.getName().equals(this.getName()) &&
                    passenger.getSurname().equals(this.getSurname()) &&
                    passenger.getProfilePicture().equals(this.getProfilePicture()) &&
                    passenger.getTelephoneNumber().equals(this.getTelephoneNumber()) &&
                    passenger.getEmail().equals(this.getEmail()) &&
                    passenger.getAddress().equals(this.getAddress());
        }
        return super.equals(obj);
    }
}
