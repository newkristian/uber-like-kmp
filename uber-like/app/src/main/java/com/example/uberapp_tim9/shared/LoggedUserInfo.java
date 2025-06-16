package com.example.uberapp_tim9.shared;

import com.example.uberapp_tim9.model.LoggedUserCredentials;

public class LoggedUserInfo {

    public static int id;
    public static String name;
    public static String surname;
    public static String email;
    //public static String profilePicture;
    public static String role;
    public static int shiftId = -1;

    public static String accessToken;
    public static String refreshToken;

    public static void cloneUserCredentials(LoggedUserCredentials credentials) {
       LoggedUserInfo.id = credentials.getId();
       LoggedUserInfo.name = credentials.getName();
       LoggedUserInfo.surname = credentials.getSurname();
       LoggedUserInfo.email = credentials.getEmail();
       //LoggedUserInfo.profilePicture = credentials.getProfilePicture();
       LoggedUserInfo.role = credentials.getRole();
    }

    public static void extractUserCredentials(String jwt) {
        LoggedUserInfo.accessToken = jwt;
        try {
            if(jwt == null) return;
            if(JwtUtils.isJWTExpired(jwt)) return;
            LoggedUserInfo.id = JwtUtils.getClaim(jwt, "id", Integer.class);
            LoggedUserInfo.name = JwtUtils.getClaim(jwt, "name", String.class);
            LoggedUserInfo.surname = JwtUtils.getClaim(jwt, "surname", String.class);
            LoggedUserInfo.email = JwtUtils.getClaim(jwt, "email", String.class);
            LoggedUserInfo.role = JwtUtils.getClaim(jwt, "role", String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
