package com.example.uberapp_tim9.model;

public class Driver extends User {
    public Driver(int mID,
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

    public Driver() {
        super();
    }

    public Driver(String mIme, String mPrezime) {
        super(mIme, mPrezime);
    }
}
