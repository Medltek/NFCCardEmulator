package com.example.nfccardemulator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mockup of acquiring access token from the server
        final byte[] testingToken1 = new byte[]{0x56, 0x65, 0x72, 0x69, 0x66, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0x20, 0x74, 0x6F, 0x6B, 0x65, 0x6E, 0x20, 0x6F, 0x66, 0x20, 0x73, 0x74, 0x75, 0x64, 0x65, 0x6E, 0x74, 0x20, 0x6E, 0x31};
        final byte[] testingToken2 = new byte[]{0x56, 0x65, 0x72, 0x69, 0x66, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0x20, 0x74, 0x6F, 0x6B, 0x65, 0x6E, 0x20, 0x6F, 0x66, 0x20, 0x73, 0x74, 0x75, 0x64, 0x65, 0x6E, 0x74, 0x20, 0x6E, 0x32};
        final byte[] testingToken3 = new byte[]{0x56, 0x65, 0x72, 0x69, 0x66, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0x20, 0x74, 0x6F, 0x6B, 0x65, 0x6E, 0x20, 0x6F, 0x66, 0x20, 0x73, 0x74, 0x75, 0x64, 0x65, 0x6E, 0x74, 0x20, 0x6E, 0x33};
        final byte[] testingToken4 = new byte[]{0x56, 0x65, 0x72, 0x69, 0x66, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0x20, 0x74, 0x6F, 0x6B, 0x65, 0x6E, 0x20, 0x6F, 0x66, 0x20, 0x73, 0x74, 0x75, 0x64, 0x65, 0x6E, 0x74, 0x20, 0x6E, 0x34};
        final byte[] testingToken5 = new byte[]{0x56, 0x65, 0x72, 0x69, 0x66, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0x20, 0x74, 0x6F, 0x6B, 0x65, 0x6E, 0x20, 0x6F, 0x66, 0x20, 0x73, 0x74, 0x75, 0x64, 0x65, 0x6E, 0x74, 0x20, 0x6E, 0x35};

        final User user1 = new User("ban0125", "heslo", testingToken1);
        final User user2 = new User("can0125", "heslo", testingToken2);
        final User user3 = new User("dan0125", "heslo", testingToken3);
        final User user4 = new User("fan0125", "heslo", testingToken4);
        final User user5 = new User("gan0125", "heslo", testingToken5);

        final TextView username =(TextView) findViewById(R.id.username);
        final TextView password =(TextView) findViewById(R.id.password);

        Button loginbtn = (Button) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") && password.getText().toString().equals("")) {
                    //correct
                    byte[] Data=user1.getTestingToken(); //define data

                    FileOutputStream fileOutputStream = null;
                    try {
                        File file = getFilesDir();
                        fileOutputStream = openFileOutput("Token.txt", Context.MODE_PRIVATE); //MODE PRIVATE
                        fileOutputStream.write(Data);
                        fileOutputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(LoginActivity.this, LoggedInActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                } else if (username.getText().toString().equals(user2.getUsername()) && password.getText().toString().equals(user2.getPassword())) {

                    byte[] Data=user2.getTestingToken(); //define data

                    FileOutputStream fileOutputStream = null;
                    try {
                        File file = getFilesDir();
                        fileOutputStream = openFileOutput("Token.txt", Context.MODE_PRIVATE); //MODE PRIVATE
                        fileOutputStream.write(Data);
                        fileOutputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(LoginActivity.this, LoggedInActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                    LoginActivity.this.startActivity(myIntent);
                } else if (username.getText().toString().equals(user3.getUsername()) && password.getText().toString().equals(user3.getPassword())) {

                    byte[] Data=user3.getTestingToken(); //define data

                    FileOutputStream fileOutputStream = null;
                    try {
                        File file = getFilesDir();
                        fileOutputStream = openFileOutput("Token.txt", Context.MODE_PRIVATE); //MODE PRIVATE
                        fileOutputStream.write(Data);
                        fileOutputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(LoginActivity.this, LoggedInActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                } else if (username.getText().toString().equals(user4.getUsername()) && password.getText().toString().equals(user4.getPassword())) {

                    byte[] Data=user4.getTestingToken(); //define data

                    FileOutputStream fileOutputStream = null;
                    try {
                        File file = getFilesDir();
                        fileOutputStream = openFileOutput("Token.txt", Context.MODE_PRIVATE); //MODE PRIVATE
                        fileOutputStream.write(Data);
                        fileOutputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(LoginActivity.this, LoggedInActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                } else if (username.getText().toString().equals(user5.getUsername()) && password.getText().toString().equals(user5.getPassword())) {

                    byte[] Data=user5.getTestingToken(); //define data

                    FileOutputStream fileOutputStream = null;
                    try {
                        File file = getFilesDir();
                        fileOutputStream = openFileOutput("Token.txt", Context.MODE_PRIVATE); //MODE PRIVATE
                        fileOutputStream.write(Data);
                        fileOutputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(LoginActivity.this, LoggedInActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                } else {
                    //incorrect
                    Toast.makeText(LoginActivity.this, "LOGIN FAILED !!!", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }




}
