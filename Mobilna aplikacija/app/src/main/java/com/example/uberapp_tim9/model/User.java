package com.example.uberapp_tim9.model;

public class User {
    private int mID;
    private String mName;
    private String mSurname;
    private String mProfilePicture;
    private String mPhoneNumber;
    private String mEmail;
    private String mAddress;
    private String mPassword;
    private boolean mIsBlocked;

    public User(int mID,
                String mName,
                String mSurname,
                String mProfilePicture,
                String mPhoneNumber,
                String mEmail,
                String mAddress,
                String mPassword,
                boolean mIsBlocked) {
        this.mID = mID;
        this.mName = mName;
        this.mSurname = mSurname;
        this.mProfilePicture = mProfilePicture;
        this.mPhoneNumber = mPhoneNumber;
        this.mEmail = mEmail;
        this.mAddress = mAddress;
        this.mPassword = mPassword;
        this.mIsBlocked = mIsBlocked;
    }

    public int getmID() {
        return mID;
    }

    public String getmName() {
        return mName;
    }

    public String getmSurname() {
        return mSurname;
    }

    public String getmProfilePicture() {
        return mProfilePicture;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmPassword() {
        return mPassword;
    }

    public boolean ismIsBlocked() {
        return mIsBlocked;
    }
}
