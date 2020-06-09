package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class DayActivity extends AppCompatActivity {

    private ListView mMainList;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");
    private String userNameLoggedIn,markerName;
    private ArrayList<User> eveUserlist;
    private TextView dateGameText;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        mMainList = (ListView) findViewById(R.id.listEv);
        mMainList.setAdapter(null);
        String year = getIntent().getStringExtra("year");
        String month = getIntent().getStringExtra("month");
        String dayOfMonth = getIntent().getStringExtra("dayOfMonth");
        final String groundName = getIntent().getStringExtra("markerName");
        final String date = dayOfMonth + "/" + month + "/" + year;
        userNameLoggedIn= getIntent().getStringExtra("userNameLoggedIn");
        markerName = getIntent().getStringExtra("markerName");
        dateGameText = (TextView)findViewById(R.id.dateGameText);
        dateGameText.setText( "משחקי ה - "+date);

        final ArrayList<Events> eventsArrayList = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ImageButton button = (ImageButton) findViewById(R.id.cButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                        Intent stAdd = new Intent(getApplicationContext(), addGameActivity.class);
                        stAdd.putExtra("date" ,date);
                        stAdd.putExtra("markerName" ,markerName);
                        stAdd.putExtra("userNameLoggedIn" ,userNameLoggedIn);

                        startActivity(stAdd);
                    }
                });
                ArrayList<String> dd = new ArrayList<>();
                String value = "";
                int i = 0;
                Map<String,Object> eventsAll = (Map<String,Object>)  dataSnapshot.getValue();//hash map for all events 0 - 50 f.e
                Object[] keysets =  eventsAll.keySet().toArray();
                for (Object key : eventsAll.values()) {
                    ArrayList<User> usersArrayList = new ArrayList<>();
                    Events event = new Events();
                        Map<String, Object > singleEvent = (Map<String, Object>) key;
                    for (Object key2 : singleEvent.keySet()) {
                        if ("date".equals(key2)) {
                            value = singleEvent.get("date").toString();
                            System.out.println(key + ":" + value);
                            event.setDate(value);
                        } else if ("ground".equals(key2)) {
                            value = singleEvent.get("ground").toString();
                            System.out.println(key + ":" + value);
                            event.setGround(value);
                        } else if ("hour".equals(key2)) {
                            value = singleEvent.get("hour").toString();
                            System.out.println(key + ":" + value);
                            event.setHour(value);
                        }
                        else if ("maxParticipants".equals(key2)){
                            value = singleEvent.get("maxParticipants").toString();
                            System.out.println(key + ":" + value);
                            event.setMaxp(value);
                        }
                        else if ("type".equals(key2)){
                            value = singleEvent.get("type").toString();
                            System.out.println(key + ":" + value);
                            event.setType(value);
                        }
                        else {
                            //if userlist
                            String value2 = "";
                            Map<String, String> listOfUsers = (Map<String, String>) singleEvent.get("userlist");

                            //mRef= mRef.child(key);
                            for (Object lists : listOfUsers.values()) {
                                User user = new User();
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
                            //if userlist
                            event.setUsername(usersArrayList);
                        }

                    }
                    String keyEvent = keysets[i].toString();
                    i++;
                    event.setId(keyEvent);
                    eventsArrayList.add(event);

                }

                eveUserlist = new ArrayList<>();
                ArrayList<String> items = new ArrayList<>();
                items.clear();

                for(Events ev : eventsArrayList){
                    if((ev.getDate().equals(date)) && (ev.getGround().equals(groundName))){
                        items.add(ev.getHour());
                        eveUserlist=ev.getUsername();
                    }
                }

                ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
                mMainList.setAdapter(null);
                mMainList.setAdapter(adapter);

                //when clicking on hour:

                mMainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                        Intent appInfo = new Intent(getApplicationContext(), UsersInGame.class);
                        appInfo.putExtra("userNameLoggedIn",userNameLoggedIn);
                        appInfo.putExtra("eventlistGame",(Serializable)eventsArrayList);
                        appInfo.putExtra("hour", (String) mMainList.getItemAtPosition(position));
                        appInfo.putExtra("markerName",markerName);
                        appInfo.putExtra("date",date);

                        startActivity(appInfo);
                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}

