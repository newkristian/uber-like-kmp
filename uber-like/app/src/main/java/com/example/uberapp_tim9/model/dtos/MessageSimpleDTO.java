package com.example.uberapp_tim9.model.dtos;

import com.example.uberapp_tim9.model.MessageType;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.model.User;

import java.time.LocalDateTime;

public class MessageSimpleDTO {
    private Integer id;
    private Integer sender;
    private Integer receiver;
    private String message;
    private LocalDateTime sentDateTime;
    private MessageType messageType;
    private Integer ride;

    public MessageSimpleDTO(Integer id, Integer sender, Integer receiver, String message, LocalDateTime sentDateTime, MessageType messageType, Integer ride) {
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

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
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

    public Integer getRide() {
        return ride;
    }

    public void setRide(Integer ride) {
        this.ride = ride;
    }
}
