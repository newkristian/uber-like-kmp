package com.example.uberapp_tim9.model;


import java.time.LocalDateTime;
import java.util.Objects;


public class User {

    private Integer id;


    private String name;

    private String surname;

    private String profilePicture;

    private String telephoneNumber;

    private String email;

    private String address;

    private String password;

    private Boolean isActivated;

    private Boolean isBlocked;

    private String resetPasswordToken;


    private LocalDateTime resetPasswordTokenExpiration;

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, Boolean isActivated, Boolean isBlocked, String resetPasswordToken, LocalDateTime resetPasswordTokenExpiration) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
        this.isActivated = isActivated;
        this.isBlocked = isBlocked;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordTokenExpiration = resetPasswordTokenExpiration;
    }

    public User(int mID,
                String mName,
                String mSurname,
                String mProfilePicture,
                String mPhoneNumber,
                String mEmail,
                String mAddress,
                String mPassword, boolean mIsBlocked) {
        this.id = mID;
        this.name = mName;
        this.surname = mSurname;
        this.profilePicture = mProfilePicture;
        this.telephoneNumber = mPhoneNumber;
        this.email = mEmail;
        this.address = mAddress;
        this.password = mPassword;
        this.isBlocked = mIsBlocked;
    }

    public User() {
    }

    public User(String mIme, String mPrezime) {
        this.name = mIme;
        this.surname = mPrezime;
    }

    public User(String mIme, String mPrezime, String mPhoneNumber) {
        this.name = mIme;
        this.surname = mPrezime;
        this.telephoneNumber = mPhoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public LocalDateTime getResetPasswordTokenExpiration() {
        return resetPasswordTokenExpiration;
    }

    public void setResetPasswordTokenExpiration(LocalDateTime resetPasswordTokenExpiration) {
        this.resetPasswordTokenExpiration = resetPasswordTokenExpiration;
    }
}
