package com.example.jaldeep.help_classes;

import java.io.Serializable;

/**
 * Created by Jaldeep on 05/04/16.
 */
public class ChatMessage implements Serializable{

    public String body, sender, receiver, senderName;
    public String Date, Time;
    public boolean isMine;// Did I send the message.

    public ChatMessage(String Sender, String Receiver, String messageString, boolean isMINE) {
        body = messageString;
        isMine = isMINE;
        sender = Sender;
        receiver = Receiver;
        senderName = sender;
    }
}