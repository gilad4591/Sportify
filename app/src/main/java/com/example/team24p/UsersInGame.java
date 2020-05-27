package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
    private ListView userView;
    private String emailUserLoggedIn,age,phone,name,maxP,type;
    private ImageView basket,tennis,soccer;
    private TextView numOfus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_game);
        numOfus = (TextView)findViewById(R.id.numOfUs);
        userView = (ListView)findViewById(R.id.userListView);
        Intent i = getIntent();
        userView.setAdapter(null);
        eventlistGame =  (ArrayList<Events>)i.getSerializableExtra("eventlistGame");
        emailUserLoggedIn = i.getStringExtra("userNameLoggedIn");
        String hour = i.getStringExtra("hour");
        String groundName = i.getStringExtra("markerName");
        String date = i.getStringExtra("date");

        basket = (ImageView)findViewById(R.id.basketball);
        tennis = (ImageView)findViewById(R.id.tennis);
        soccer = (ImageView)findViewById(R.id.soccer);


        //String emailUserLoggedIn = "";
        Button joinButton = (Button)findViewById(R.id.joinButton);
        String key = null;
        for(Events ev : eventlistGame){
            if((ev.getDate().equals(date)) && (ev.getGround().equals(groundName)) && (ev.getHour().equals(hour))){//the selected event
                key = ev.getId();
                UserArrayList = ev.getUsername();
                maxP = ev.getMaxP();
                type = ev.getType();
                if(type.equals("כדורגל"))soccer.setVisibility(View.VISIBLE);
                if(type.equals("כדורסל"))basket.setVisibility(View.VISIBLE);
                if(type.equals("טניס"))tennis.setVisibility(View.VISIBLE);
            }
        }
        numOfus.setText(UserArrayList.size() + "/" + maxP);

        if((emailUserLoggedIn!=null))
        {
            for(User us:UserArrayList){//if exist in game already
                if(emailUserLoggedIn.equals(us.getUserName()))flag=1;
            }
            if(flag==1)joinButton.setVisibility(View.INVISIBLE);
            else if(Integer.parseInt(maxP)<=UserArrayList.size()){
                joinButton.setVisibility(View.INVISIBLE);
                Toast.makeText(UsersInGame.this, "לא ניתן להצטרף כרגע, מספר המשתתפים שהצטרפו למשחק הינו מקסימלי" , Toast.LENGTH_SHORT).show();
            }
        }
        else {
            joinButton.setVisibility(View.INVISIBLE);
            Toast.makeText(UsersInGame.this, "אינך מחובר \n נא להתחבר על מנת להצטרף למשחק" , Toast.LENGTH_SHORT).show();
        }


        mRef = mDatabase.getReference().child("Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : usersList.keySet()) {
                    Map<String, Object> value = (HashMap<String, Object>) usersList.get(key);
                    if(emailUserLoggedIn!=null) {
                        if (value.get("username").toString().equals(emailUserLoggedIn)) {
                            age = value.get("age").toString();
                            phone = value.get("phone").toString();
                            name = value.get("Name").toString();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef = mDatabase.getReference().child("Events").child(key).child("userlist");

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object > rate = new HashMap<String, Object>();
                rate.put("age", age);
                rate.put("email", emailUserLoggedIn);
                rate.put("name", name);
                rate.put("phone", phone);
                if(flag!=1){
                    mRef.push().updateChildren(rate);
                }
                finish();
            }
        });


        final ArrayList<String> items = new ArrayList<>();
        items.clear();

                for (User us : UserArrayList) {

                    items.add(us.getName() + "  -  " + us.getPhoneNumber());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
                userView.setAdapter(null);
                userView.setAdapter(adapter);



    }
}
