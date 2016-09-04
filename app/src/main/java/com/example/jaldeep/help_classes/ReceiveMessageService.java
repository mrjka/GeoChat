package com.example.jaldeep.help_classes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.jaldeep.entities.ReceiveMessage;
import com.example.jaldeep.geochat.AllChats;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by paulosk on 08/04/16.
 */
public class ReceiveMessageService extends Service {

    private String mUserID;

    private ServerCommunication mServerCommunication;

    private ReceiverThread mUpdatingThread;

    private static volatile boolean mServiceRunning;

    private String userID;


    private static class ReceiverThread extends Thread {

        private final Context context;
        private ServerCommunication serverCommunication;
        private final String userID;

        public ReceiverThread(Context context, String userID) {
            this.userID = userID;
            this.context = context;
            serverCommunication = new ServerCommunication();
        }

        public void run() {
            while (mServiceRunning) {

                ReceiveMessage[] msgs = serverCommunication.receiveMessage(userID);
                ArrayList<ChatMessage> recvMsgs = new ArrayList<>(msgs.length);

                for (int i = 0; i < msgs.length; i++) {
                    ReceiveMessage receivedMessage = msgs[i];
                    recvMsgs.add(new ChatMessage(receivedMessage.getSender(), userID, receivedMessage.getMessage(), false));
                }

                for (int i = 0; i < recvMsgs.size(); i++) {
                    ChatMessage msg = recvMsgs.get(i);
                    if (AllChats.chats.containsKey(msg.sender)) {
                        AllChats.chats.get(msg.sender).add(msg);
                        Log.e("Debug", "Added message from " + msg.sender + " with message " + msg.body + " to hashmap");
                    } else {
                        AllChats.chats.put(msg.sender, new ConcurrentLinkedQueue<ChatMessage>());
                    }
                }

                /*
                Intent intent = new Intent();
                intent.setAction("ReceiveMessage");
                intent.putExtra("ReceivedMessages", recvMsgs);
                Log.e("Debug", "Made a broadcast with an array with size " + recvMsgs.size());

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*/


                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }


    @Override
    public void onCreate() {
        mServerCommunication = new ServerCommunication();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userID = intent.getStringExtra("UserID");
        mUpdatingThread = new ReceiverThread(this, userID);
        mServiceRunning = true;
        mUpdatingThread.start();
        return 0;
    }

    @Override
    public void onDestroy() {
        mServiceRunning = false;
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
