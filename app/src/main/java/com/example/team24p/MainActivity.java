package com.example.team24p;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    static int count = 0;
    private TextView welcomeTextView;

    private Button adminPanelButton;
    private String userNameLoggedIn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        adminPanelButton = (Button) findViewById(R.id.buttonManagePanel);
        FloatingActionButton logoutButton = (FloatingActionButton) findViewById(R.id.logoutButton);
        adminPanelButton.setVisibility(View.INVISIBLE);
        logoutButton.setVisibility(View.INVISIBLE);

        try {
            userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
            welcomeTextView.setText("");
        } catch (Exception e){
        }
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stNav = new Intent(getApplicationContext(), MapsActivity.class);
                stNav.putExtra("userNameLoggedIn",userNameLoggedIn);
                startActivity(stNav);
            }
        });
            FloatingActionButton LoginButton = (FloatingActionButton) findViewById(R.id.LoginButton);
            if (count != 0){
                count++;
            }
            if (userNameLoggedIn != null){
                LoginButton.setVisibility(View.INVISIBLE);
                logoutButton.setVisibility(View.VISIBLE);
                welcomeTextView.setText("Welcome"+" " +(userNameLoggedIn));
                //if user admin make visible button admin
                if (getIntent().getStringExtra("isAdmin").equals("True")){
                    adminPanelButton.setVisibility(View.VISIBLE);
                }
            }

            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent stLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    userNameLoggedIn = "";
                    startActivity(stLogin);
                }
            });

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    intent.removeExtra("isAdmin");
                    intent.removeExtra("userNameLoggedIn");
                    finish();
                    startActivity(intent);
                }
            });

        adminPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stAdmin = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(stAdmin);
            }
        });
    }
}
