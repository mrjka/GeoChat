package com.example.jaldeep.geochat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaldeep.help_classes.CustomList;

import java.util.ArrayList;

/**
 * Created by Jaldeep on 08/04/16.
 */
public class AllChatsTab2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //private User user = new User("Jal", 20, "Jal@my.com", "I am the shit");

    //List for the names
    //private User[] users = {user, user, user, user, user};

    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<String> ages = new ArrayList<>();

    private View view;

    public static CustomList listAdapterTab2;


    public AllChatsTab2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllChatsTab1.
     */
    // TODO: Rename and change types and number of parameters
    public static AllChatsTab1 newInstance(int param1) {
        AllChatsTab1 fragment = new AllChatsTab1();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_chats_tab2, container, false);

        names = new ArrayList<>();
        ages = new ArrayList<>();

        //Get the userID from intent
        Intent intent = getActivity().getIntent();
        String userID = intent.getStringExtra("UserID");

        listAdapterTab2 = new CustomList(getActivity(), names, ages, false, userID);
        ListView list = (ListView) view.findViewById(R.id.listAllChatsTab2);
        list.setAdapter(listAdapterTab2);


        return view;
    }

    /**
     * Update the list whenever new user is added
     */
    public static void updateList(String name, String age)    {
        boolean isAlreadyInList = false;
        for(int i = 0; i < names.size(); i++)   {
            if(names.get(i).equals(name) && ages.get(i).equals(age))    {
                isAlreadyInList = true;
            }
        }
        if(!isAlreadyInList) {
            names.add(name);
            ages.add(age);
            listAdapterTab2.add(name, age);
            listAdapterTab2.notifyDataSetChanged();
        }

    }

}
