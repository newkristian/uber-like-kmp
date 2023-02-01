package com.example.uberapp_tim9.shared;

import com.example.uberapp_tim9.model.LoggedUserCredentials;

public class LoggedUserInfo {

    public static int id;
    public static String name;
    public static String surname;
    public static String email;
    public static String profilePicture;
    public static String role;

    public static void cloneUserCredentials(LoggedUserCredentials credentials) {
       LoggedUserInfo.id = credentials.getId();
       LoggedUserInfo.name = credentials.getName();
       LoggedUserInfo.surname = credentials.getSurname();
       LoggedUserInfo.email = credentials.getEmail();
       LoggedUserInfo.profilePicture = credentials.getProfilePicture();
       LoggedUserInfo.role = credentials.getRole();
    }
}
