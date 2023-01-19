package com.example.uberapp_tim9.model;

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
}
