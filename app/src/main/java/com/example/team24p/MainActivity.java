package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    static int count = 0;
    private TextView welcomeTextView;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");
    private FloatingActionButton adminPanelButton;
    private String userNameLoggedIn = "";
    private ArrayList<String> items;
    private ListView myAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        adminPanelButton = (FloatingActionButton) findViewById(R.id.buttonManagePanel);
        FloatingActionButton logoutButton = (FloatingActionButton) findViewById(R.id.logoutButton);
        adminPanelButton.setVisibility(View.INVISIBLE);
        logoutButton.setVisibility(View.INVISIBLE);
        myAct = (ListView)findViewById(R.id.myActivityList);
        myAct.setVisibility(View.INVISIBLE);

        FloatingActionButton menuButton = (FloatingActionButton)findViewById(R.id.menuButton);

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
                myAct.setVisibility(View.VISIBLE);
                welcomeTextView.setText("Welcome"+" " +(userNameLoggedIn));
                //if user admin make visible button admin
                if (getIntent().getStringExtra("isAdmin").equals("True")){
                    adminPanelButton.setVisibility(View.VISIBLE);
                }

            }
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent stMenu = new Intent(getApplicationContext(),todayGamesActivity.class);
                    if(userNameLoggedIn!=null)stMenu.putExtra("username",userNameLoggedIn);
                    startActivity(stMenu);
                }
            });

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
        items.clear();
        if(userNameLoggedIn!=null){
            mRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String d = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/M/yy"));
                    Map<String, Object> eventsAll = (Map<String, Object>) dataSnapshot.getValue();//hash map for all events 0 - 50 f.e
                    for (Object key : eventsAll.values()) {
                        Map<String, Object> singleEvent = (Map<String, Object>) key;
                        for (Object key2 : singleEvent.keySet()) {
                            if((key2.toString().equals("userlist"))&&(singleEvent.get("date").toString() .compareTo(d)>=0)){
                                Map<String, String> listOfUsers = (Map<String, String>) singleEvent.get("userlist");

                                for (Object lists : listOfUsers.values()) {
                                    Map<String, String> singleUser = (Map<String, String>) lists;
                                    for (Object key3 : singleUser.keySet()) {
                                        if ("email".equals(key3.toString())) {
                                            if (userNameLoggedIn.equals(singleUser.get("email").toString())) {
                                                String x =
                                                        singleEvent.get("date").toString() + " " +
                                                                singleEvent.get("ground").toString() + " " +
                                                                singleEvent.get("hour").toString();
                                                items.add(x);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
                    myAct.setAdapter(null);
                    myAct.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
}
