package com.example.jaldeep.geochat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaldeep.entities.User;
import com.example.jaldeep.help_classes.CustomList;
import com.example.jaldeep.help_classes.ServerCommunication;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllChatsTab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllChatsTab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllChatsTab1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //List for the names
    private User[] users = {};

    CustomList listAdapter;

    String userID;

    //private List<User> users;


    public AllChatsTab1() {
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
        View view = inflater.inflate(R.layout.fragment_all_chats_tab, container, false);

        //Get the userID from intent
        Intent intent = getActivity().getIntent();
        userID = intent.getStringExtra("UserID");


        //Get the referesh button
        Button refersh = (Button) view.findViewById(R.id.refereshButtonAllChatsTab);
        refersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the list of users
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(userID);


                AllChatsTab2.listAdapterTab2.clear();
                AllChatsTab2.names = new ArrayList<String>();
                AllChatsTab2.ages = new ArrayList<String>();

            }
        });
        //Get the list of users
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(userID);


        //Populate the list with users
        List<String> name = new ArrayList<>();
        List<String> age = new ArrayList<>();
        for (int i = 0; i < users.length; i++) {
            name.add(i, users[i].getName());
            age.add(users[i].getAge() + "");
        }

        listAdapter = new CustomList(getActivity(), name, age, true, userID);
        ListView list = (ListView) view.findViewById(R.id.listAllChatsTab);
        list.setAdapter(listAdapter);


        return view;
    }

    public void addToList() {
        //Populate the list with users
        List<String> name = new ArrayList<>();
        List<String> age = new ArrayList<>();
        for (int i = 0; i < users.length; i++) {
            name.add(i, users[i].getName());
            age.add(users[i].getAge() + "");
        }

        listAdapter.setName(name);
        listAdapter.setAge(age);
        listAdapter.notifyDataSetChanged();
    }

    public class AsyncTaskRunner extends AsyncTask<String, Void, User[]> {

        @Override
        protected User[] doInBackground(String... params) {
            //Create a new server to communicate with
            ServerCommunication server = new ServerCommunication();
            return server.listAllUsers(params[0]);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(User[] allUsers) {
            if (users != null) {
                users = allUsers;
                addToList();
            } else {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class LocationPublish extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            ServerCommunication server = new ServerCommunication();
            return server.publishLocation(params[0], params[1], params[2]);
        }
    }

}
