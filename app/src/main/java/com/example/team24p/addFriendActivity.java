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
    public final ArrayList<User>userArrayList = new ArrayList<>();
    private ListView userListSearched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
        searchLine = getIntent().getStringExtra("searchLine");

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

                userListSearched.setAdapter(null);
                userListSearched.setAdapter(new ListResources(addFriendActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    class ListResources extends BaseAdapter {
        ArrayList<User>mydata;
        User temp;
        int flag;
        Context context;
        String selectedKey1,selectedKey2;

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
            mRef = mDatabase.getReference().child("Friends");

            accBut.setVisibility(View.VISIBLE);
            pending.setVisibility(View.INVISIBLE);
            TextView userTextViewList = (TextView)row.findViewById(R.id.userTextViewAdd);
            userTextViewList.setText(temp.getUserName());
            flag = 0;
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> friendsTable = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : friendsTable.keySet()) {
                        Map<String, Object> value = (HashMap<String, Object>) friendsTable.get(key);
                        if (value.get("username").toString().equals(temp.getUserName())) {
                            Map<String, Object> friendlist = (HashMap<String, Object>) value.get("friendlist");
                            for (String key2 : friendlist.keySet()) {
                                Map<String, Object> value2 = (HashMap<String, Object>) friendlist.get(key2);

                                if (value2.get("username").equals(userNameLoggedIn) && value2.get("enabled").toString() == "true") {
                                    accBut.setVisibility(View.INVISIBLE);
                                    pending.setVisibility(View.INVISIBLE);
                                }
                                temp.setId(key);
                            }
                            flag=1;
                        }
                    }
                    accBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(flag!=1){
                                Map<String, Object> user = new HashMap<>();
                                Map<String, Object> newFriend = new HashMap<>();
                                Map<String, Object> friendlist = new HashMap<>();
                                newFriend.put("enabled",true);
                                newFriend.put("confirmed",false);
                                newFriend.put("username",userNameLoggedIn);
                                String key = mRef.push().getKey();

                                user.put(key,newFriend);
                                friendlist.put("friendlist",user);
                                friendlist.put("username",temp.getUserName());

                                mRef.child(key).setValue(friendlist);
                            }
                            else {
                                Map<String, Object> newFriend = new HashMap<>();
                                newFriend.put("enabled",true);
                                newFriend.put("confirmed",false);
                                newFriend.put("username",userNameLoggedIn);
                                String key = mRef.push().getKey();
                                mRef.child(temp.getId()).child("friendlist").child(key).setValue(newFriend);
                            }




                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return row;

        }
    }
}
