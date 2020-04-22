package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class todayGamesActivity extends AppCompatActivity {
    private  ListView myAct;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_games);
        final String username;
        username = getIntent().getStringExtra("username");
        final ArrayList<Events> eventsArrayList = new ArrayList<>();

        myAct = (ListView)findViewById(R.id.gamesToday);
        items = new ArrayList<>();
        items.clear();
        myAct.setAdapter(null);
        mRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String d = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/M/yy"));
                Map<String, Object> eventsAll = (Map<String, Object>) dataSnapshot.getValue();//hash map for all events 0 - 50 f.e
                int i = 1;
                Object[] keysets =  eventsAll.keySet().toArray();
                for (Object key : eventsAll.values()) {
                    ArrayList<User> usersArrayList = new ArrayList<>();
                    Events event = new Events();
                    Map<String, Object> singleEvent = (Map<String, Object>) key;
                    for (Object key2 : singleEvent.keySet()) {
                        if((key2.toString().equals("userlist"))&&(singleEvent.get("date").toString() .compareTo(d)>=0)){
                            Map<String, String> listOfUsers = (Map<String, String>) singleEvent.get("userlist");
                            String x = singleEvent.get("date").toString() + " - " +
                                    singleEvent.get("ground").toString() + " - " +
                                    singleEvent.get("hour").toString();
                            items.add(x);

                            for (Object lists : listOfUsers.values()) {
                                User user = new User();
                                String value2=" ";
                                Map<String, String> singleUser = (Map<String, String>) lists;
                                for(Object key3 : singleUser.keySet())
                                    if ("age".equals(key3.toString())) {
                                        value2 = singleUser.get("age").toString();
                                        user.setAge(value2);
                                    } else if ("name".equals(key3.toString())) {
                                        value2 = singleUser.get("name").toString();
                                        user.setName(value2);
                                    } else if ("phone".equals(key3.toString())) {
                                        value2 = singleUser.get("phone").toString();
                                        user.setPhoneNumber(value2);
                                    } else if ("email".equals(key3.toString())) {
                                        value2 = singleUser.get("email").toString();
                                        user.setUserName(value2);

                                    }

                                usersArrayList.add(user);
                            }

                            event.setHour(singleEvent.get("hour").toString());
                            event.setGround(singleEvent.get("ground").toString());
                            event.setDate(singleEvent.get("date").toString());
                            event.setUsername(usersArrayList);
                            String keyEvent = keysets[i].toString();
                            i++;
                            event.setId(keyEvent);
                            eventsArrayList.add(event);
                        }


                    }

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
                myAct.setAdapter(null);
                myAct.setAdapter(adapter);}



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) myAct.getItemAtPosition(position);
                String x[] = str.split(" - ",3);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Intent appInfo = new Intent(getApplicationContext(), UsersInGame.class);
                appInfo.putExtra("userNameLoggedIn",username);
                appInfo.putExtra("eventlistGame",(Serializable)eventsArrayList);
                appInfo.putExtra("hour", x[2] );
                appInfo.putExtra("markerName",x[1]);
                appInfo.putExtra("date",x[0]);

                startActivity(appInfo);
            }
        });

    }


}