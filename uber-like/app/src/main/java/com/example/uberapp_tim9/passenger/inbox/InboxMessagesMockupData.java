package com.example.uberapp_tim9.passenger.inbox;

import com.example.uberapp_tim9.model.Message;
import com.example.uberapp_tim9.model.MessageType;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.model.Path;
import com.example.uberapp_tim9.model.Review;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InboxMessagesMockupData {
    public static List<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();
        User u1 = new User();
        u1.setName("Support");
        Message m1 = new Message(1, "Da li Vam je potrebna pomoc?",
                LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 30),
                u1, new User(), MessageType.SUPPORT, new Ride());

        User u2 = new User();
        u2.setName("Mika");
        Message m2 = new Message(2, "Stizem za 5 minuta!",
                LocalDateTime.of(2022, Month.DECEMBER, 15, 10, 0),
                u2, new User(), MessageType.VOZNJA, new Ride());

        User u3 = new User();
        u3.setName("Branislav");
        Message m3 = new Message(3, "UPOMOOOOOOOOOC!!!!!",
                LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 30),
                u3, new User(), MessageType.PANIC, new Ride());

        User u4 = new User();
        u4.setName("Natalija");
        Message m4 = new Message(4, "Gospodine, ne radimo dostavu",
                LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 30),
                u4, new User(), MessageType.VOZNJA, new Ride());

        User u5 = new User();
        u5.setName("Petar");
        Message m5 = new Message(5, "Uspesno otkazano!",
                LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 30),
                u5, new User(), MessageType.PANIC, new Ride());

        messages.add(m1);
        messages.add(m2);
        messages.add(m3);
        messages.add(m4);
        messages.add(m5);

        return messages;
    }
}
