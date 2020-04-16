package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersInGame extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef;
    private ArrayList<Events> eventlistGame;
    private ArrayList<User> UserArrayList;
    private int flag = 0;
    private String emailUserLoggedIn,age,phone,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_game);

        ListView userView = (ListView)findViewById(R.id.userListView);
        Intent i = getIntent();
        eventlistGame =  (ArrayList<Events>)i.getSerializableExtra("eventlistGame");
        emailUserLoggedIn = i.getStringExtra("userNameLoggedIn");
        String hour = i.getStringExtra("hour");
        String groundName = i.getStringExtra("markerName");
        String date = i.getStringExtra("date");

        //String emailUserLoggedIn = "";
        Button joinButton = (Button)findViewById(R.id.joinButton);
        String key = null;
        for(Events ev : eventlistGame){
            if((ev.getDate().equals(date)) && (ev.getGround().equals(groundName)) && (ev.getHour().equals(hour))){//the selected event
                UserArrayList = ev.getUsername();
                key = ev.getId();
            }
        }

        if((emailUserLoggedIn!=null))
        {
            for(User us:UserArrayList){//if exist in game already
                if(emailUserLoggedIn.equals(us.getUserName()))flag=1;
            }
            if(flag==1)joinButton.setVisibility(View.INVISIBLE);
        }
        else {
            joinButton.setVisibility(View.INVISIBLE);
            Toast.makeText(UsersInGame.this, "You are not logged in. \n log in before join game" , Toast.LENGTH_SHORT).show();
        }



        ArrayList<String> items = new ArrayList<>();
        for(User us : UserArrayList){
                items.add(us.getName());
            }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
        userView.setAdapter(adapter);
        mRef = mDatabase.getReference().child("Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : usersList.keySet()) {
                    Map<String, Object> value = (HashMap<String, Object>) usersList.get(key);
                    //if (email.equalsIgnoreCase(value.get("UserName").toString()) && password.equals(value.get("Password").toString())) {
                    if (value.get("username").toString().equals(emailUserLoggedIn)) {
                        age=value.get("age").toString();
                        phone=value.get("phone").toString();
                        name=value.get("Name").toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef = mDatabase.getReference().child("Events").child(key.toString()).child("userlist");

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object > rate = new HashMap<String, Object>();
                rate.put("age", age);
                rate.put("email", emailUserLoggedIn);
                rate.put("name", name);
                rate.put("phone", phone);
                if(flag!=1) mRef.push().updateChildren(rate);
            }
        });



    }
}
