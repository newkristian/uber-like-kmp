package com.example.uberapp_tim9.model;

import java.time.LocalDateTime;

public class Message {
    private int mID;
    private String mMessage;
    private LocalDateTime mTimeSent;
    private User mSender;
    private User mReciever;
    private MessageType mMessageType;
    private Ride mRide;

    public Message(int mID,
                   String mMessage,
                   LocalDateTime mTimeSent,
                   User mSender,
                   User mReciever,
                   MessageType mMessageType,
                   Ride mRide) {
        this.mID = mID;
        this.mMessage = mMessage;
        this.mTimeSent = mTimeSent;
        this.mSender = mSender;
        this.mReciever = mReciever;
        this.mMessageType = mMessageType;
        this.mRide = mRide;
    }

    public int getmID() {
        return mID;
    }

    public String getmMessage() {
        return mMessage;
    }

    public LocalDateTime getmTimeSent() {
        return mTimeSent;
    }

    public User getmSender() {
        return mSender;
    }

    public User getmReciever() {
        return mReciever;
    }

    public MessageType getmMessageType() {
        return mMessageType;
    }

    public Ride getmRide() {
        return mRide;
    }
}
