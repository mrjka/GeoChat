package com.example.jaldeep.geochat;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.jaldeep.help_classes.ChatMessage;
import com.example.jaldeep.help_classes.LocationUpdatingServices;
import com.example.jaldeep.help_classes.ReceiveMessageService;
import com.example.jaldeep.help_classes.ServerCommunication;
import com.example.jaldeep.help_classes.SlidingTabLayout;
import com.example.jaldeep.help_classes.TabsFragmentPageAdapter;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AllChats extends AppCompatActivity {
// Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    TabsFragmentPageAdapter adapter;
    com.example.jaldeep.help_classes.SlidingTabLayout tabs;
    CharSequence Titles[] = {"All Chats", "My Chats"};
    int Numboftabs = 2;

    public static HashMap<String, ConcurrentLinkedQueue<ChatMessage>> chats;

    private ServerCommunication server;

    private String userID;

    private Intent intentLocationService;
    private Intent intentReceiveMsg;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);

        chats = new HashMap<>();

        Intent intent = getIntent();
        userID = intent.getStringExtra("UserID");

        //Starts the service for publishing the location
        intentLocationService = new Intent(AllChats.this, LocationUpdatingServices.class);
        intentLocationService.putExtra("UserID", userID);
        startService(intentLocationService);


        //start the receive messages
        intentReceiveMsg = new Intent(AllChats.this, ReceiveMessageService.class);
        intentReceiveMsg.putExtra("UserID", userID);
        startService(intentReceiveMsg);

        /*
        IntentFilter filter = new IntentFilter();
        filter.addAction("ReceiveMessage");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("Debug", "In the broadcast receiver");
                ArrayList<ChatMessage> newMessages = (ArrayList<ChatMessage>) intent.getSerializableExtra("ReceiveMessages");
                if(newMessages != null) {
                    for (int i = 0; i < newMessages.size(); i++) {
                        ChatMessage msg = newMessages.get(i);
                        Log.e("Debug", "Received a new message from " + msg.sender);
                        if (chats.containsKey(msg.sender)) {
                            chats.get(msg.sender).add(msg);
                        } else {
                            chats.put(msg.sender, new ArrayList<ChatMessage>());
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, filter);*/

        //Listener for new chat messages
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("ReceiveMessage"));

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new TabsFragmentPageAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pagerAllChats);
        pager.setAdapter(adapter);


        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabsAllChats);
        tabs.setDistributeEvenly(true);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intentLocationService);
        stopService(intentReceiveMsg);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(userID);

    }

    public class AsyncTaskRunner extends AsyncTask<String, Void, Boolean>   {
        @Override
        protected void onPostExecute(Boolean response) {
            if(response)    {
                Toast.makeText(AllChats.this, "You have been logged out!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ServerCommunication server = new ServerCommunication();
            return server.logout(params[0]);
        }
    }
}
