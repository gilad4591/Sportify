package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addFriendActivity extends AppCompatActivity {

    private String userNameLoggedIn;
    private String searchLine;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef;
    private ArrayList<User>userArrayList;
    private ListView userListSearched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
        searchLine = getIntent().getStringExtra("searchLine");
        userArrayList = new ArrayList<>();
        mRef= mDatabase.getReference().child("Users");
        userListSearched = (ListView)findViewById(R.id.SearchedList);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object > userTable = (HashMap<String, Object>)  dataSnapshot.getValue();
                for (String key : userTable.keySet()) {
                    Map<String, Object> value = (HashMap<String, Object>) userTable.get(key);
                    User user = new User();
                    if (value.get("username").toString().contains(searchLine)) {
                        user.setUserName(value.get("username").toString());
                        userArrayList.add(user);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userListSearched.setAdapter(null);
        userListSearched.setAdapter(new ListResources(addFriendActivity.this));

    }
    class ListResources extends BaseAdapter {
        ArrayList<User>mydata;
        User temp;
        Context context;
        ListResources(Context context){
            this.context = context;
            mydata=userArrayList;

        }
        @Override
        public int getCount() {
            return userArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return userArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =getLayoutInflater();
            View row = inflater.inflate(R.layout.addrow,parent,false);
            final TextView pending = (TextView)row.findViewById(R.id.pendingText);
            final FloatingActionButton accBut = (FloatingActionButton)row.findViewById(R.id.addFriendBut);
            temp = mydata.get(position);
            accBut.setVisibility(View.VISIBLE);
            pending.setVisibility(View.INVISIBLE);
            TextView userTextViewList = (TextView)row.findViewById(R.id.userTextViewAdd);
            userTextViewList.setText(temp.getUserName());

            accBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pending.setVisibility(View.VISIBLE);
                    accBut.setVisibility(View.INVISIBLE);
                }
            });

            return row;

        }
    }
}
