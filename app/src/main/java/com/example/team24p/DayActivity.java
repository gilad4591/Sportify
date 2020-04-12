package com.example.team24p;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DayActivity extends AppCompatActivity {
    private static final String TAG = "FireLog";
    private RecyclerView mMainList;
    private FirebaseFirestore mFirestore;
    private EventListAdapter eventListAdapter;
    private List<Events> eventsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
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
        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.d(TAG,"Error : " +e.getMessage());
                }
                for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges() ){
                    //compare
                   Events events = doc.getDocument().toObject(Events.class);
                   eventsList.add(events);
                   eventListAdapter.notifyDataSetChanged();

                }
            }
        });
        //select from data base events from day and ground


    }
}

