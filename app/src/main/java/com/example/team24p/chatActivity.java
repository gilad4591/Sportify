package com.example.team24p;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scaledrone.lib.Scaledrone;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class chatActivity extends AppCompatActivity {


    private EditText editText;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private String userNameLoggedIn,userSelected;
    public MemberData data;
    public String selctedKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mRef = mDatabase.getReference().child("messages");
        editText = (EditText) findViewById(R.id.editText);
        userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
        userSelected = getIntent().getStringExtra("userSelected");
        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);         
        messagesView.setAdapter(null);
        messagesView.setAdapter(messageAdapter);


        data = new MemberData(userSelected, getRandomColor());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> messageTable = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : messageTable.keySet()) {
                    Map<String, Object> value = (HashMap<String, Object>) messageTable.get(key);
                    if ((value.get("user1").toString().equals(userNameLoggedIn)&&value.get("user2").toString().equals(userSelected))||((value.get("user1").toString().equals(userSelected)&&value.get("user2").toString().equals(userNameLoggedIn)))){
                        selctedKey = key;
                        Map<String, Object> messageList = (HashMap<String, Object>) value.get("messageList");
                        for (String key2 : messageList.keySet()) {
                            Map<String, Object> value2 = (HashMap<String, Object>) messageList.get(key2);

                            if (value2.get("sender").equals(userNameLoggedIn)) {
                                onMessage(value2.get("text").toString(),true);
                            }
                            else onMessage(value2.get("text").toString(),false);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(View view){
        final String msg = editText.getText().toString();
        final Map<String, String> userData = new HashMap<String, String>();
        final Map<String, String> userData2 = new HashMap<String, String>();
        String time = LocalDateTime.now().toString();
        if(selctedKey!=null) {
            if (msg.length() > 0) {
                editText.getText().clear();

                //onMessage(msg,true);

                userData.put("sender", userNameLoggedIn);
                userData.put("text", msg);
                userData.put("timeStamp", time);
                String key = mRef.push().getKey();
                DatabaseReference refChildKey = mRef.child(selctedKey).child("messageList").child(key);
                refChildKey.setValue(userData);
                messageAdapter.notifyDataSetChanged();
            }
        }
        else{
            if (msg.length() > 0) {
                editText.getText().clear();

                //onMessage(msg,true);
                userData.put("sender", userNameLoggedIn);
                userData.put("text", msg);
                userData.put("timeStamp", time);
                userData2.put("user1",userNameLoggedIn);
                userData2.put("user2",userSelected);

                String key = mRef.push().getKey();
                String key2 = mRef.push().getKey();

                DatabaseReference refChildKey2 = mRef.child(key);
                refChildKey2.setValue(userData2);
                DatabaseReference refChildKey = mRef.child(key).child("messageList").child(key2);
                refChildKey.setValue(userData);

                messageAdapter.notifyDataSetChanged();
            }
        }
    }
    public void onMessage(final String msg, final boolean belg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Message message = new Message(msg,data,belg);
                messageAdapter.add(message);
                // scroll the ListView to the last added element
                messagesView.setSelection(messagesView.getCount() - 1);
            }
        });
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
}

class MemberData {
    private String name;
    private String color;

    public MemberData(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public MemberData() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}