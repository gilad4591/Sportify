package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class messageActivity extends AppCompatActivity {

    private String userNameLoggedIn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef;
    private ArrayList<String> userConfirmed;
    private ArrayList<String> userNotConfirmed;
    private ListView confirmedList;
    private ListView notConList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mRef = mDatabase.getReference().child("Friends");
        Intent i = getIntent();
         confirmedList = (ListView)findViewById(R.id.friendsListView);
         notConList = (ListView)findViewById(R.id.pendingListView);

        userNameLoggedIn= i.getStringExtra("userNameLoggedIn");
        userConfirmed = new ArrayList<>();
        userNotConfirmed = new ArrayList<>();



        //get data of friends - confirmed and not confirmed and set it to two lists
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object > friendsTable = (HashMap<String, Object>)  dataSnapshot.getValue();
                for (String key : friendsTable.keySet()) {
                    Map<String, Object> value = (HashMap<String, Object>) friendsTable.get(key);
                    if(value.get("username").toString().equals(userNameLoggedIn)){
                        Map<String, Object> friendlist = (HashMap<String, Object>) value.get("friendlist");
                        for (String key2 : friendlist.keySet()) {
                            Map<String, Object> value2 = (HashMap<String, Object>) friendlist.get(key2);

                            if (value2.get("confirmed").toString().equals("true")&& value2.get("enabled").toString() =="true") {
                                userConfirmed.add(value2.get("username").toString()); //set the confirmed list if the user already friend
                            } else if (value2.get("confirmed").toString().equals("false") && value2.get("enabled").toString()=="true"){
                                userNotConfirmed.add(value2.get("username").toString()); //set the unconfirmed list
                            }
                        }
                        break;
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,userConfirmed);
                confirmedList.setAdapter(null);
                confirmedList.setAdapter(adapter);

                notConList.setAdapter(null);
                notConList.setAdapter(new ListResources(messageActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //if the user press search he moves to the add friend activity so he can add new friend
        ImageButton searchB = (ImageButton) findViewById(R.id.searchButton);
        final TextInputEditText lineToSearch = (TextInputEditText)findViewById(R.id.searchFriends);
        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = lineToSearch.getText().toString().trim();
                if(s!=null){
                    Intent inte = getIntent();
                    finish();
                    startActivity(inte);

                    Intent intent = new Intent(messageActivity.this,addFriendActivity.class); // move to add friend activity
                    intent.putExtra("searchLine",s);
                    intent.putExtra("userNameLoggedIn",userNameLoggedIn);
                    startActivity(intent);
                }
                else
                    Toast.makeText(messageActivity.this,"Enter a line first",Toast.LENGTH_SHORT).show();

            }
        });

        //if user click on friend he initiate the chat activity with the selected friend
        confirmedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent stChat = new Intent(messageActivity.this,chatActivity.class);
                stChat.putExtra("userSelected",(String)confirmedList.getItemAtPosition(position));
                stChat.putExtra("userNameLoggedIn",userNameLoggedIn);
                startActivity(stChat);
            }
        });
    }


    //custom adapter of x / v pending friend request
    class ListResources extends BaseAdapter{
        ArrayList<String>mydata;
        String temp;
        Context context;
        ListResources(Context context){
            this.context = context;
            mydata=userNotConfirmed;

        }
        @Override
        public int getCount() {
            return userNotConfirmed.size();
        }

        @Override
        public Object getItem(int position) {
            return userNotConfirmed.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =getLayoutInflater();
            View row = inflater.inflate(R.layout.listrow,parent,false);
            TextView usText = (TextView)row.findViewById(R.id.usernameText);
            FloatingActionButton accBut = (FloatingActionButton)row.findViewById(R.id.acceptButton);
            FloatingActionButton decBut = (FloatingActionButton)row.findViewById(R.id.declineButton);

            temp = mydata.get(position);

            //if user click confirm button
            accBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> friendsTable = (HashMap<String, Object>) dataSnapshot.getValue();
                            for (String key : friendsTable.keySet()) {
                                Map<String, Object> value = (HashMap<String, Object>) friendsTable.get(key);
                                //set the new friend on my firebase table
                                if (value.get("username").toString().equals(userNameLoggedIn)) {
                                    Map<String, Object> friendlist = (HashMap<String, Object>) value.get("friendlist");
                                    for (String key2 : friendlist.keySet()) {
                                        Map<String, Object> value2 = (HashMap<String, Object>) friendlist.get(key2);

                                        if (value2.get("username").equals(temp) && value2.get("enabled").toString() == "true") {
                                            value2.put("confirmed",true); //change from request to confirm friend
                                            DatabaseReference refChildKey = mRef.child(key).child("friendlist").child(key2);
                                            refChildKey.setValue(value2);
                                        }
                                    }
                                }
                                //set me on the new friend firebase table
                                if(value.get("username").toString().equals(temp)){
                                    Map<String, Object> friendlist = (HashMap<String, Object>) value.get("friendlist");
                                    for (String key2 : friendlist.keySet()) {
                                        Map<String, Object> value2 = (HashMap<String, Object>) friendlist.get(key2);

                                        if (value2.get("username").equals(userNameLoggedIn) && value2.get("enabled").toString() == "false") {
                                            value2.put("confirmed",true);
                                            value2.put("enabled",true);
                                            DatabaseReference refChildKey = mRef.child(key).child("friendlist").child(key2);
                                            refChildKey.setValue(value2);
                                        }
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Intent inet =getIntent();
                    finish();
                    startActivity(inet);


                }
            });
            //if user click decline button
            decBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> friendsTable = (HashMap<String, Object>) dataSnapshot.getValue();
                            for (String key : friendsTable.keySet()) {
                                Map<String, Object> value = (HashMap<String, Object>) friendsTable.get(key);
                                if (value.get("username").toString().equals(userNameLoggedIn)) {
                                    Map<String, Object> friendlist = (HashMap<String, Object>) value.get("friendlist");
                                    for (String key2 : friendlist.keySet()) {
                                        Map<String, Object> value2 = (HashMap<String, Object>) friendlist.get(key2);
                                        //set the request to off!
                                        if (value2.get("username").equals(temp) && value2.get("enabled").toString() == "true") {
                                            value2.put("enabled",false);
                                            DatabaseReference refChildKey = mRef.child(key).child("friendlist").child(key2);
                                            refChildKey.setValue(value2);
                                        }
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Intent inet =getIntent();
                    finish();
                    startActivity(inet);
                }
            });

            usText.setText(temp);
            return row;
        }
    }
}
