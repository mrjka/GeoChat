package com.example.jaldeep.geochat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jaldeep.help_classes.ServerCommunication;

public class ProfilePage extends AppCompatActivity {
    ServerCommunication server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        //Get the values user entered in the register page
        final Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");
        final String password = intent.getStringExtra("Password");
        final String email = intent.getStringExtra("Email");

        //Add a button listener that sends all the info from profile page + register user to server
        Button confirm = (Button) findViewById(R.id.ConfirmButtonProfilePage);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get value from the three editText boxes
                EditText nameText = (EditText) findViewById(R.id.NameProfilePage);
                String name = nameText.getText().toString();

                EditText ageText = (EditText) findViewById(R.id.AgeProfilePage);
                String age = ageText.getText().toString();

                EditText description = (EditText) findViewById(R.id.DescriptionProfilePage);
                String desc = description.getText().toString();

                if(name.equals("") || age.equals("") || desc.equals(""))    {
                    Toast.makeText(ProfilePage.this, "Please fill in all the required information!", Toast.LENGTH_LONG).show();
                } else if(age.length() > 3) {
                    Toast.makeText(ProfilePage.this, "You cannot enter a age this big! Please try again!", Toast.LENGTH_LONG).show();
                } else  {
                    //Try to register the user
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.execute(username, password, email, name, age, desc);
                }

            }
        });


    }


    public class AsyncTaskRunner extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            //Create a new server to communicate with
            server = new ServerCommunication();
            return server.registerUser(params[0], params[1], params[2], params[3], params[4], params[5]);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Boolean response) {
            Log.e("Debug", "We received a response: " + response);
            if(response)  {
                Intent intent2 = new Intent(ProfilePage.this, LoginPage.class);
                startActivity(intent2);
            } else  {
                Toast.makeText(ProfilePage.this, "Something went wrong! The user could not be created. Please try again!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
