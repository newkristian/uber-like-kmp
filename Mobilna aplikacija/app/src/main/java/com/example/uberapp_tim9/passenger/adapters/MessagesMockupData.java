package com.example.uberapp_tim9.passenger.adapters;

import com.example.uberapp_tim9.model.Driver;
import com.example.uberapp_tim9.model.Message;
import com.example.uberapp_tim9.model.MessageType;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MessagesMockupData {

    public static List<Message> messages = getMessages();

    private static List<Message> getMessages() {

        Ride ride = new Ride(1);
        User driver = new User(4);
        User passenger = new User(1);
        List<Message> messages = new ArrayList<>();
        Message message_one = new Message(1,
                "Da li se približavamo lokaciji?",
                LocalDateTime.now().minus(15, ChronoUnit.MINUTES),
                passenger,
                driver,
                MessageType.VOZNJA,
                ride);

        Message message_two = new Message(2,
                "Za minut-dva smo tamo",
                LocalDateTime.now().minus(13, ChronoUnit.MINUTES),
                driver,
                passenger,
                MessageType.VOZNJA,
                ride);

        Message message_three = new Message(3,
                "Okej, hvala",
                LocalDateTime.now().minus(12, ChronoUnit.MINUTES),
                passenger,
                driver,
                MessageType.VOZNJA,
                ride);
        messages.add(message_one);
        messages.add(message_two);
        messages.add(message_three);
        return messages;
    }
}
