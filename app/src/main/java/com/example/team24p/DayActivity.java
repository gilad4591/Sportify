package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayActivity extends AppCompatActivity {

    private ListView mMainList;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");
    private String userNameLoggedIn,markerName;
    private ArrayList<User> eveUserlist;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        mMainList = (ListView) findViewById(R.id.listEv);
        String year = getIntent().getStringExtra("year");
        String month = getIntent().getStringExtra("month");
        String dayOfMonth = getIntent().getStringExtra("dayOfMonth");
        final String groundName = getIntent().getStringExtra("markerName");
        final String date = dayOfMonth + "/" + month + "/" + year;
        userNameLoggedIn= getIntent().getStringExtra("userNameLoggedIn");
        markerName = getIntent().getStringExtra("markerName");


        final ArrayList<Events> eventsArrayList = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ImageButton button = (ImageButton) findViewById(R.id.addButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent stAdd = new Intent(getApplicationContext(), addGameActivity.class);
                        stAdd.putExtra("date" ,date);
                        stAdd.putExtra("markerName" ,markerName);
                        stAdd.putExtra("userNameLoggedIn" ,userNameLoggedIn);

                        startActivity(stAdd);
                    }
                });
                ArrayList<String> dd = new ArrayList<>();
                String value = "";

                ArrayList<Map<String,Object>> events = (ArrayList<Map<String,Object>>)  dataSnapshot.getValue();//hash map for all events 0 - 50 f.e
                for (Object key : events.toArray()) {
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
                        } else {
                            //if userlist
                            String value2 = "";
                            ArrayList<Map<String, String>> listOfUsers = (ArrayList<Map<String, String>>) singleEvent.get("userlist");

                            //mRef= mRef.child(key);
                            for (Object lists : listOfUsers.toArray()) {
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

                    event.setId(key.toString());
                    eventsArrayList.add(event);

                }

                eveUserlist = new ArrayList<>();
                ArrayList<String> items = new ArrayList<>();
                for(Events ev : eventsArrayList){
                    if((ev.getDate().equals(date)) && (ev.getGround().equals(groundName))){
                        items.add(ev.getHour());
                        eveUserlist=ev.getUsername();
                    }
                }

                ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
                mMainList.setAdapter(adapter);

                //when clicking on hour:

                mMainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

