package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class addGameActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");
    private String date,hour,ground,username;
    private String text,age,phone,name;
    private TextInputEditText hourText;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        date = getIntent().getStringExtra("date");
        ground = getIntent().getStringExtra("markerName");
        username = getIntent().getStringExtra("userNameLoggedIn");

        hourText = (TextInputEditText)findViewById(R.id.hourInput);
        TextInputEditText dateText = (TextInputEditText)findViewById(R.id.dateInput);
        TextInputEditText groundText = (TextInputEditText)findViewById(R.id.groundInput);


        dateText.setText(date);
        groundText.setText(ground);


        final Spinner numOfParticipants = findViewById(R.id.maxPart);
        String [] items = new String[]{"1","2","3","4","5","6","7","8","9","10+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,items);
        numOfParticipants.setAdapter(adapter);
        text = numOfParticipants.getSelectedItem().toString();


        if(username!=null) {
            mRef = mDatabase.getReference().child("Users");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : usersList.keySet()) {
                        Map<String, Object> value = (HashMap<String, Object>) usersList.get(key);
                        if (value.get("username").toString().equals(username)) {
                            age = value.get("age").toString();
                            phone = value.get("phone").toString();
                            name = value.get("Name").toString();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        Button addGame = (Button)findViewById(R.id.gameAddcButton);
        if(username==null || username == ""){
            addGame.setVisibility(View.INVISIBLE);
            Toast.makeText(addGameActivity.this, "You are not logged in. \n log in before create a game" , Toast.LENGTH_SHORT).show();
        }

        mRef = mDatabase.getReference().child("Events");
        addGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                hour = hourText.getText().toString().trim();
                if (!hour.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
                    hourText.setError("Please enter valid hour");
                    hourText.requestFocus();
                } else {

                    Map<String, Object> games = new HashMap<String, Object>();
                    Map<String, Object> User = new HashMap<String, Object>();
                    Map<String, Object> Users = new HashMap<String, Object>();
                    text = numOfParticipants.getSelectedItem().toString();

                    games.put("date", date);
                    games.put("ground", ground);
                    games.put("hour", hour);
                    games.put("userlist", Users);
                    games.put("maxParticipants",text);
                    String key = mRef.push().getKey();
                    mRef.child(key).setValue(games);

                    mRef = mDatabase.getReference().child("Events").child(key).child("userlist");
                    User.put("age", age);
                    User.put("phone", phone);
                    User.put("name", name);
                    User.put("email", username);

                    mRef.push().updateChildren(User);
                    Toast.makeText(addGameActivity.this, "The game schedule successfully" , Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });







    }

}
