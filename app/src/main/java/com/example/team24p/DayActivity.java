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
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DayActivity extends AppCompatActivity {
    //private static final String TAG = "FireLog";
    private ListView mMainList;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");
    private String userNameLoggedIn;
    //private EventListAdapter eventListAdapter;
    private List<Events> eventsList;
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
/*
        eventsList = new ArrayList<>();
        eventListAdapter = new EventListAdapter(eventsList);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(this));
        mMainList = (RecyclerView) findViewById(R.id.event_list);
        mMainList.setAdapter(eventListAdapter);
*/

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
                        stAdd.putExtra("groundName" ,date);
                        stAdd.putExtra("date" ,date);
                        stAdd.putExtra("userNameLoggedIn" ,userNameLoggedIn);

                        startActivity(stAdd);
                    }
                });



                ArrayList<Map<String, String>> events = (ArrayList<Map<String, String>>) dataSnapshot.getValue();
                ArrayList<User> userArrayList =new ArrayList<>();
                ArrayList<String> stringslist =new ArrayList<>();
                String value="";
                for (Map<String, String> entry : events) {
                    Events event = new Events();
                    for (String key : entry.keySet()) {
                   //     for (int i = 0; i < 4; i++) {

                            switch (key) {
                                case "date":
                                     value = entry.get(key);
                                    System.out.println(key + ":" + value);
                                    event.setDate(value);

                                    break;
                                case "ground":
                                     value = entry.get(key);
                                    System.out.println(key + ":" + value);
                                    event.setGround(value);
                                    break;
                                case "hour":
                                     value = entry.get(key);
                                    System.out.println(key + ":" + value);
                                    event.setHour(value);

                                    break;

                                default:
                                   mRef= mRef.child("userlist");


                                        User user = new User();
                                        for (int x = 0; x < 3; x++) {
                                            mRef= mRef.child("0");
                                            String value2 = entry.get(key);
                                            stringslist.add(entry.get(key));
                                            System.out.println(key + ":" + value2);
                                            switch (key) {
                                                case "age":
                                                    user.setAge(value2);
                                                    System.out.println(key + ":" + value2);

                                                    break;
                                                case "name":
                                                    user.setName(value2);
                                                    System.out.println(key + ":" + value2);

                                                    break;

                                                case "phone":
                                                    user.setPhoneNumber(value2);
                                                    System.out.println(key + ":" + value2);
                                                    break;
                                            }
                                        }
                                        mRef=mRef.getParent();
                                        userArrayList.add(user);

                                    break;
                                            }
                                      //  }



                    }
                    eventsArrayList.add(event);

                }


                ArrayList<String> items = new ArrayList<>();
                for(Events ev : eventsArrayList){
                    if((ev.getDate().equals(date)) && (ev.getGround().equals(groundName))){
                        items.add(ev.getHour());
                    }
                }

                ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
                mMainList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}

