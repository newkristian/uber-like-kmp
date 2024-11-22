package com.example.uberapp_tim9.model.dtos;


public class PassengerIdEmailDTO {

    private Integer id;
    private String email;

    public PassengerIdEmailDTO(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public PassengerIdEmailDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
