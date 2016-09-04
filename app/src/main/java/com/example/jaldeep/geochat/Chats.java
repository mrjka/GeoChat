package com.example.jaldeep.geochat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.jaldeep.entities.ResponseType;
import com.example.jaldeep.entities.SendMessage;
import com.example.jaldeep.help_classes.ChatAdapter;
import com.example.jaldeep.help_classes.ChatMessage;
import com.example.jaldeep.help_classes.CommonMethods;
import com.example.jaldeep.help_classes.ServerCommunication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Chats extends AppCompatActivity {
    private EditText msg_edittext;
    public static String user1ID = "";
    public static String user2Name = "";
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;
    String message = "";
    private boolean receivingMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        Intent intent = getIntent();
        user1ID = intent.getStringExtra("User1");
        user2Name = intent.getStringExtra("User2");

        receivingMessage = true;
        AsyncTaskReceiveMessage receiveRunner = new AsyncTaskReceiveMessage();
        receiveRunner.execute();

        //Enter the text in this Edit Text
        msg_edittext = (EditText) findViewById(R.id.messageLayoutChat);

        //List view to show the messages
        msgListView = (ListView) findViewById(R.id.msgListViewLayoutChat);

        //The send button
        ImageButton sendButton = (ImageButton) findViewById(R.id.sendMessageButtonLayoutChat);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = msg_edittext.getEditableText().toString();
                if (!message.equalsIgnoreCase("")) {
                    new Thread()    {
                        @Override
                        public void run() {
                            super.run();
                            ServerCommunication server = new ServerCommunication();
                            SendMessage msg = server.sendMessage(user1ID, user2Name, message);
                            if (msg.getResponseType() == ResponseType.SUCCESS) {
                                Log.e("Debug", "The message was sent successfully");
                            } else  {
                                Log.e("Debug", "The message was not sent successfully");
                            }
                        }
                    }.start();

                    sendTextMessage();
                }
            }
        });



        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        //List for the chat
        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(Chats.this, chatlist);
        msgListView.setAdapter(chatAdapter);
    }



    /**
     * Method for sending message
     *
     * @param
     */
    public void sendTextMessage() {
        final ChatMessage chatMessage = new ChatMessage(user1ID, user2Name, message, true);
        chatMessage.body = message;
        chatMessage.Date = CommonMethods.getCurrentDate();
        chatMessage.Time = CommonMethods.getCurrentTime();
        msg_edittext.setText("");
        chatAdapter.add(chatMessage);
        chatAdapter.notifyDataSetChanged();

    }

    public void receiveMessage()    {
        HashMap<String, ConcurrentLinkedQueue<ChatMessage>> chats = AllChats.chats;
        Log.e("Debug", "Trying to get message from " + user2Name);
        if(chats.containsKey(user2Name)) {
            ConcurrentLinkedQueue<ChatMessage> msgRec = chats.get(user2Name);
            ChatMessage chatMessage;
            while((chatMessage = msgRec.poll()) != null) {
                chatMessage.Date = CommonMethods.getCurrentDate();
                chatMessage.Time = CommonMethods.getCurrentTime();
                chatAdapter.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receivingMessage = false;
    }

    public class AsyncTaskReceiveMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while(receivingMessage) {
                if (AllChats.chats.containsKey(user2Name)) {
                    if (AllChats.chats.get(user2Name).size() > 0) {
                        Log.e("Debug", "Received a new message automatically");
                        publishProgress();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.e("Debug", "Displaying the message");
            receiveMessage();
        }
    }


}
