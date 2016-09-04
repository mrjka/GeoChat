package com.example.jaldeep.help_classes;

/**
 * Created by Jaldeep on 05/04/16.
 */
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jaldeep.geochat.R;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<ChatMessage> chatMessageList;

    public ChatAdapter(Activity activity, ArrayList<ChatMessage> list) {
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the current message to display
        ChatMessage message = (ChatMessage) chatMessageList.get(position);

        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.bubble_chat, null);

        //Set the text in the bubble
        TextView msg = (TextView) view.findViewById(R.id.messageBubbleChat);
        msg.setText(message.body);


        //Get the layouts from bubble_chat xml file
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.bubble_layout);
        LinearLayout parent_layout = (LinearLayout) view.findViewById(R.id.bubble_layout_parent);

        // if message is mine then align to right
        if (message.isMine) {
            layout.setBackgroundResource(R.drawable.bubble2);
            parent_layout.setGravity(Gravity.RIGHT);
        }
        // If not mine then align to left
        else {
            layout.setBackgroundResource(R.drawable.bubble1);
            parent_layout.setGravity(Gravity.LEFT);
        }
        return view;
    }

    public void add(ChatMessage object) {
        chatMessageList.add(object);
    }
}