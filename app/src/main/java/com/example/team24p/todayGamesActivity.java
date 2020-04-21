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
        mRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String d = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/M/yy"));
                Map<String, Object> eventsAll = (Map<String, Object>) dataSnapshot.getValue();//hash map for all events 0 - 50 f.e
                int i = 0;
                Object[] keysets =  eventsAll.keySet().toArray();
                for (Object key : eventsAll.values()) {
                    Events event = new Events();
                    Map<String, Object> singleEvent = (Map<String, Object>) key;
                    for (Object key2 : singleEvent.keySet()) {
                        if((key2.toString().equals("userlist"))&&(singleEvent.get("date").toString() .compareTo(d)>=0)){
                            Map<String, String> listOfUsers = (Map<String, String>) singleEvent.get("userlist");
                            String x = singleEvent.get("date").toString() + " " +
                                    singleEvent.get("ground").toString() + " " +
                                    singleEvent.get("hour").toString();
                            items.add(x);
                            event.setHour(singleEvent.get("hour").toString());
                            event.setGround(singleEvent.get("ground").toString());
                            event.setDate(singleEvent.get("date").toString());

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
                String x[] = str.split(" ",3);
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