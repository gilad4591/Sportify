package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

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
    private static final String TAG = "FireLog";
    private RecyclerView mMainList;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");

    private EventListAdapter eventListAdapter;
    private List<Events> eventsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        mMainList = (RecyclerView) findViewById(R.id.event_list);
        String year = getIntent().getStringExtra("year");
        String month = getIntent().getStringExtra("month");
        String dayOfMonth = getIntent().getStringExtra("dayOfMonth");
        String groundName = getIntent().getStringExtra("markerName");

        eventsList = new ArrayList<>();
        eventListAdapter = new EventListAdapter(eventsList);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(this));
        mMainList = (RecyclerView) findViewById(R.id.event_list);
        mMainList.setAdapter(eventListAdapter);



        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Map<String, String>> events = (ArrayList<Map<String, String>>) dataSnapshot.getValue();
                for (Map<String, String> entry : events) {
                    for (String key : entry.keySet()) {
                        String value = entry.get(key);
                        System.out.println(key + ":" + value);

                        Events event = new Events(value,value,value);
                        eventsList.add(event);
                        eventListAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}

