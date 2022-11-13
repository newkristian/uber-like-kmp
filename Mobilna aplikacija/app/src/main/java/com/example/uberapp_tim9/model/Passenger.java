package com.example.uberapp_tim9.model;

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
}
