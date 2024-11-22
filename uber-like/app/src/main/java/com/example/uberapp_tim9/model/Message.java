package com.example.uberapp_tim9.model;

import java.time.LocalDateTime;

public class Message {
    
    private Integer id;

    private User sender;
    
    private User receiver;

    private String message;

    private LocalDateTime sentDateTime;

    private MessageType messageType;
    
    private Ride ride;

    public Message(Integer id,
                   String message,
                   LocalDateTime sentDateTime,
                   User sender,
                   User receiver,
                   MessageType messageType,
                   Ride ride) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sentDateTime = sentDateTime;
        this.messageType = messageType;
        this.ride = ride;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentDateTime() {
        return sentDateTime;
    }

    public void setSentDateTime(LocalDateTime sentDateTime) {
        this.sentDateTime = sentDateTime;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
