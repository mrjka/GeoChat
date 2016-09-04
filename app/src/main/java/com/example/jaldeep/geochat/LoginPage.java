package com.example.jaldeep.geochat;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaldeep.entities.Login;
import com.example.jaldeep.entities.ResponseType;
import com.example.jaldeep.help_classes.ServerCommunication;

public class LoginPage extends AppCompatActivity {
    ServerCommunication server;

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //Set the editTexts
        username = (EditText) findViewById(R.id.UserNameLoginPage);
        password = (EditText) findViewById(R.id.PasswordLoginPage);

        //Set the onClickListener for login button
        Button loginBtn = (Button) findViewById(R.id.LoginButtonLoginPage);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the error message to empty string
                TextView errorMessage = (TextView) findViewById(R.id.ErrorMessageLoginPage);
                errorMessage.setText("");

                //Get username
                String userID = username.getText().toString();

                //Get password
                String pw = password.getText().toString();

                if(userID.equals("") || pw.equals(""))  {
                    Toast.makeText(LoginPage.this, "Please fill in all the required information!", Toast.LENGTH_LONG).show();
                } else if(userID.contains(" ") || pw.contains(" ")) {
                    Toast.makeText(LoginPage.this, "Username and Password cannot contain whitespaces!", Toast.LENGTH_LONG).show();
                } else {

                    //Try to log in
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.execute(userID, pw);
                }

            }
        });

        //Set button listener for when the user presses "Register Here"
        Button registerBtn = (Button) findViewById(R.id.RegisterHereButtonLoginPage);
        registerBtn.setVisibility(View.VISIBLE);
        registerBtn.setBackgroundColor(Color.TRANSPARENT);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterUser.class);
                startActivity(intent);
            }
        });

        //Set button listener for the forgot password button
        Button forgotPassBtn = (Button) findViewById(R.id.ForgotPasswordButtonLoginPage);
        forgotPassBtn.setVisibility(View.VISIBLE);
        forgotPassBtn.setBackgroundColor(Color.TRANSPARENT);
        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginPage.this, "This feature has not yet been implemented", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class AsyncTaskRunner extends AsyncTask<String, Void, Login> {

        @Override
        protected Login doInBackground(String... params) {
            //Create a new server to communicate with
            server = new ServerCommunication();
            return server.login(params[0], params[1]);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Login login) {
            if(login.getResponseType().equals(ResponseType.ERROR))  {
                //Set the error message
                TextView errorMessage = (TextView) findViewById(R.id.ErrorMessageLoginPage);
                errorMessage.setText(login.getErrorMessage());

                //Clear the edit text fields
                username.setText("");
                password.setText("");
            } else  {
                //Go to the next screen
                Intent intent = new Intent(LoginPage.this, AllChats.class);
                intent.putExtra("UserID", login.getDynamicID());
                Log.e("Debug", "My userID is " + login.getDynamicID());
                startActivity(intent);
            }
        }
    }
}
