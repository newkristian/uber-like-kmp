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

    public User() {

    }

    public User(String mName, String mSurname) {
        this.mName = mName;
        this.mSurname = mSurname;
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

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public void setmProfilePicture(String mProfilePicture) {
        this.mProfilePicture = mProfilePicture;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }
}
