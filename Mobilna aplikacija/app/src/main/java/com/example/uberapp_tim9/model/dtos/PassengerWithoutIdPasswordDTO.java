package com.example.uberapp_tim9.model.dtos;

import com.example.uberapp_tim9.model.Passenger;

public class PassengerWithoutIdPasswordDTO {
    public String name;
    public String surname;
    public String profilePicture;
    public String telephoneNumber;
    public String email;
    public String address;

    public PassengerWithoutIdPasswordDTO(Passenger passenger) {
        this.name = passenger.getName();
        this.surname = passenger.getSurname();
        this.profilePicture = passenger.getProfilePicture();
        this.telephoneNumber = passenger.getTelephoneNumber();
        this.email = passenger.getEmail();
        this.address = passenger.getAddress();
    }

    public PassengerWithoutIdPasswordDTO(String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
}
