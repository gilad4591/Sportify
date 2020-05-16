package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    static int count = 0;
    private TextView welcomeTextView;
    private TextView editPrivateText;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");
    private FloatingActionButton adminPanelButton;
    private FloatingActionButton messageBut;
    private FloatingActionButton helpBut;
    private String userNameLoggedIn = "";
    private ArrayList<String> items;
    private View help;
    private ListView myAct;
    private TextView firstText;
    private TextView mesText;
    private TextView logoutText;
    private TextView loginText;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        items.clear();
        messageBut = (FloatingActionButton) findViewById(R.id.messageButton);
        firstText = (TextView)findViewById(R.id.firstText);
        messageBut.setVisibility(View.INVISIBLE);
        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        editPrivateText = (TextView)findViewById(R.id.editPrivateText);
        mesText = (TextView)findViewById(R.id.mestext);
        logoutText = (TextView)findViewById(R.id.logouttext);
        loginText = (TextView)findViewById(R.id.logtext);
        editPrivateText.setFocusable(false);
        editPrivateText.setClickable(true);
        editPrivateText.setVisibility(View.INVISIBLE);
        firstText.setVisibility(View.VISIBLE);
        mesText.setVisibility(View.INVISIBLE);
        logoutText.setVisibility(View.INVISIBLE);
        loginText.setVisibility(View.VISIBLE);
        helpBut = (FloatingActionButton)findViewById(R.id.helpBut);
        help = (View)findViewById(R.id.helpView);

        helpBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(help);
            }
        });


        adminPanelButton = (FloatingActionButton) findViewById(R.id.buttonManagePanel);
        FloatingActionButton logoutButton = (FloatingActionButton) findViewById(R.id.logoutButton);
        adminPanelButton.setVisibility(View.INVISIBLE);
        logoutButton.setVisibility(View.INVISIBLE);
        myAct = (ListView)findViewById(R.id.myActivityList);
        myAct.setVisibility(View.INVISIBLE);
        myAct.setAdapter(null);

        FloatingActionButton menuButton = (FloatingActionButton)findViewById(R.id.menuButton);
        welcomeTextView.setText("");
        try {
            userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
            if (getIntent().getStringExtra("isAdmin").equals("True")){
                adminPanelButton.setVisibility(View.VISIBLE);
            }
        } catch (Exception e){
        }
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);

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
                firstText.setVisibility(View.INVISIBLE);
                LoginButton.setVisibility(View.INVISIBLE);
                loginText.setVisibility(View.INVISIBLE);
                mesText.setVisibility(View.VISIBLE);
                logoutText.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.VISIBLE);
                myAct.setVisibility(View.VISIBLE);
                welcomeTextView.setText("Welcome"+" " +(userNameLoggedIn));
                editPrivateText.setVisibility(View.VISIBLE);
                messageBut.setVisibility(View.VISIBLE);
                //if user admin make visible button admin
            }
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
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

        editPrivateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stSelfEditDetails = new Intent(getApplicationContext(), EditUserDetailsActivity.class);
                stSelfEditDetails.putExtra("userNameLoggedIn",userNameLoggedIn);
                stSelfEditDetails.putExtra("userToEdit",userNameLoggedIn);
                stSelfEditDetails.putExtra("isAdmin",getIntent().getStringExtra("isAdmin"));
                startActivity(stSelfEditDetails);
            }
        });
        adminPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stAdmin = new Intent(getApplicationContext(), AdminActivity.class);
                stAdmin.putExtra("userNameLoggedIn",userNameLoggedIn);
                stAdmin.putExtra("isAdmin",getIntent().getStringExtra("isAdmin"));
                startActivity(stAdmin);
            }
        });
        items.clear();
        if(userNameLoggedIn!=null){
            mRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String d = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/M/yyyy"));
                    Date d1,d2;
                    DateFormat dtf = new SimpleDateFormat("dd/M/yyyy");
                    d1 = dtf.parse(d,new ParsePosition(0));


                    Map<String, Object> eventsAll = (Map<String, Object>) dataSnapshot.getValue();//hash map for all events 0 - 50 f.e
                    for (Object key : eventsAll.values()) {
                        Map<String, Object> singleEvent = (Map<String, Object>) key;
                        for (Object key2 : singleEvent.keySet()) {
                            d2 = dtf.parse(singleEvent.get("date").toString(),new ParsePosition(0));
                            if((key2.toString().equals("userlist"))&&(d2.compareTo(d1)>=0)) {
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
        messageBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stMess = new Intent(MainActivity.this,messageActivity.class);
                stMess.putExtra("userNameLoggedIn",userNameLoggedIn);
                startActivity(stMess);
            }
        });

    }
    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.
                popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        FloatingActionButton cb = popupView.findViewById(R.id.closeBut);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }
}
