package com.example.jaldeep.help_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaldeep.geochat.AllChatsTab2;
import com.example.jaldeep.geochat.Chats;
import com.example.jaldeep.geochat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaldeep on 07/04/16.
 */
public class CustomList extends BaseAdapter {
    private static LayoutInflater inflater = null;

    private final Activity context;
    private List<String> name;
    private List<String> age;
    private boolean isTab1;
    private String userID;

    public CustomList(Activity context, List<String> name, List<String> age, boolean isTab1, String userID) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.name = name;
        this.age = age;
        this.isTab1 = isTab1;
        this.userID = userID;
    }

    @Override
    public int getCount() {
        return name.size();
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
    public View getView(final int position, View view, ViewGroup parent) {
        View rowView= inflater.inflate(R.layout.list_element, null, true);

        TextView nameText = (TextView) rowView.findViewById(R.id.NameListElement);
        nameText.setText("Name: " + name.get(position));

        TextView ageText = (TextView) rowView.findViewById(R.id.AgeListElement);
        ageText.setText("Age: " + age.get(position));

        ImageView profilePicture = (ImageView) rowView.findViewById(R.id.UserPhotoListElement);
        profilePicture.setImageResource(R.drawable.profile_photo);

        ImageButton chatBtn = (ImageButton) rowView.findViewById(R.id.ChatButtonListElement);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTab1)  {
                    AllChatsTab2.updateList(name.get(position), age.get(position));
                }
                Intent intent = new Intent(context, Chats.class);
                intent.putExtra("User1", userID);
                intent.putExtra("User2", name.get(position));
                context.startActivity(intent);

            }
        });

        return rowView;
    }

    public void add(String nameString, String ageString)    {
        Toast.makeText(context, "Succesfully added user to \"My Chats\" ", Toast.LENGTH_LONG).show();
        name.add(nameString);
        age.add(ageString);
    }

    public void clear() {
        name = new ArrayList<>();
        age = new ArrayList<>();
    }

    public void setName(List<String> n)    {
        name = n;
    }

    public void setAge(List<String> a) {
        age = a;
    }
}
