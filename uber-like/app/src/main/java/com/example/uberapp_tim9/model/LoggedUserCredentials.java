package com.example.uberapp_tim9.model;

public class LoggedUserCredentials {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String name;
    private String surname;
    private String email;

    public LoggedUserCredentials(int id, String name, String surname, String email, String profilePicture, String role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.profilePicture = profilePicture;
        this.role = role;
    }

    private String profilePicture;
    private String role;
}
