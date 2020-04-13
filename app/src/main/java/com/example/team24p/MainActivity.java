package com.example.team24p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    static int count = 0;
    private TextView welcomeTextView;
    private String userNameLoggedIn = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);



        try {
            userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
            welcomeTextView.setText("");
        } catch (Exception e){
        }
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stNav = new Intent(getApplicationContext(), MapsActivity.class);
                stNav.putExtra("userNameLoggedIn",userNameLoggedIn);
                startActivity(stNav);
            }
        });
            Button LoginButton = (Button) findViewById(R.id.LoginButton);
            if (count != 0){
                count++;
            }
            if (userNameLoggedIn != null){
                LoginButton.setVisibility(View.INVISIBLE);
                welcomeTextView.setText("Welcome"+" " +(userNameLoggedIn));
            }

            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent stLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(stLogin);
                }
            });
    }
}
