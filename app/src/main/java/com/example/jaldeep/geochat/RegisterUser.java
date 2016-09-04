package com.example.jaldeep.geochat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //Get the value from fields on button click and save them to intent and go to profile page
        Button registerBtn = (Button) findViewById(R.id.RegisterButtonRegisterUser);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get value from the three editText boxes
                EditText username = (EditText) findViewById(R.id.UserNameRegisterUser);
                String user = username.getText().toString();

                EditText password = (EditText) findViewById(R.id.PasswordRegisterUser);
                String pass = password.getText().toString();

                EditText email = (EditText) findViewById(R.id.EmailRegisterUser);
                String mail = email.getText().toString();

                if(user.equals("") || pass.equals("") || mail.equals(""))   {
                    Toast.makeText(RegisterUser.this, "Please fill in all the required information!", Toast.LENGTH_LONG).show();
                } else if(user.contains(" ") || pass.contains(" ") || mail.contains(" ")) {
                    Toast.makeText(RegisterUser.this, "Please remove the whitespaces from username/password/email!", Toast.LENGTH_LONG).show();
                } else if(!mail.contains("@") || !mail.contains(".")) {
                    Toast.makeText(RegisterUser.this, "The entered email address is not valid, please try again!", Toast.LENGTH_LONG).show();
                }   else {

                    Intent intent = new Intent(RegisterUser.this, ProfilePage.class);
                    intent.putExtra("Username", user);
                    intent.putExtra("Password", pass);
                    intent.putExtra("Email", mail);
                    startActivity(intent);
                }
            }
        });
    }
}
