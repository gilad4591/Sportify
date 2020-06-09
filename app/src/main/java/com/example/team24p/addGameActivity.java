package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
    private DatabaseReference mRef;
    private String date,hour,ground,username;
    private String text,age,phone,name;
    private TextInputEditText hourText;
    private ImageView basketOff;
    private ImageView tennisOff;
    private ImageView soccerOff;
    private ImageView basketOn;
    private ImageView tennisOn;
    private ImageView soccerOn;

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

        basketOff = findViewById(R.id.basket_off);
        basketOn = findViewById(R.id.basket_on);
        tennisOff = findViewById(R.id.tennis_off);
        tennisOn = findViewById(R.id.tennis_on);
        soccerOff = findViewById(R.id.soccer_off);
        soccerOn = findViewById(R.id.soccer_on);

        basketOn.setVisibility(View.INVISIBLE);
        basketOff.setVisibility(View.INVISIBLE);
        tennisOff.setVisibility(View.INVISIBLE);
        tennisOn.setVisibility(View.INVISIBLE);
        soccerOff.setVisibility(View.INVISIBLE);
        soccerOn.setVisibility(View.INVISIBLE);

        mRef = mDatabase.getReference().child("locations");
        mRef.orderByChild("Name").equalTo(ground).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<Object, Object > location = (HashMap<Object, Object>)  dataSnapshot.getValue();
                for(Object key: location.keySet()) {
                    Map<String, String > key2 = (HashMap<String, String>) location.get(key);
                    if (key2.get("Type").equals("כדורגל")) soccerOn.setVisibility(View.VISIBLE);
                    else if (key2.get("Type").equals("כדורסל"))
                        basketOn.setVisibility(View.VISIBLE);
                    else if (key2.get("Type").equals("משולב")) {
                        soccerOn.setVisibility(View.VISIBLE);
                        basketOn.setVisibility(View.VISIBLE);
                    } else if (key2.get("Type").equals("טניס"))
                        tennisOn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef = mDatabase.getReference().child("Events");

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
            Toast.makeText(addGameActivity.this, "אינך מחובר, התחבר על מנת ליצור משחק" , Toast.LENGTH_SHORT).show();
        }

        mRef = mDatabase.getReference().child("Events");
        addGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                hour = hourText.getText().toString().trim();
                if (!hour.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
                    hourText.setError("נא להזין שעה תקינה");
                    hourText.requestFocus();
                }
                else if(soccerOff.getVisibility()==View.INVISIBLE && basketOff.getVisibility()==View.INVISIBLE && tennisOff.getVisibility()==View.INVISIBLE)
                {
                    Toast.makeText(addGameActivity.this, "נא לבחור סוג משחק לפני יצירתו" , Toast.LENGTH_SHORT).show();
                }
                else {

                    Map<String, Object> games = new HashMap<String, Object>();
                    Map<String, Object> User = new HashMap<String, Object>();
                    Map<String, Object> Users = new HashMap<String, Object>();
                    text = numOfParticipants.getSelectedItem().toString();
                    String type="";
                    if(soccerOff.getVisibility()==View.VISIBLE) type="כדורגל";
                    if(basketOff.getVisibility()==View.VISIBLE) type="כדורסל";
                    if(tennisOff.getVisibility()==View.VISIBLE) type="טניס";



                    games.put("date", date);
                    games.put("ground", ground);
                    games.put("hour", hour);
                    games.put("type",type);
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

    public void click_basket(View view) {
        if(basketOn.getVisibility()==View.VISIBLE){
            basketOff.setVisibility(View.VISIBLE);
            basketOn.setVisibility(View.INVISIBLE);
        }
        else {
            basketOn.setVisibility(View.VISIBLE);
            basketOff.setVisibility(View.INVISIBLE);
        }

        if(basketOff.getVisibility()==View.VISIBLE){
            if(tennisOff.getVisibility()==View.VISIBLE){
                tennisOff.setVisibility(View.INVISIBLE);
                tennisOn.setVisibility(View.VISIBLE);
            }
            else if(soccerOff.getVisibility()==View.VISIBLE) {
                soccerOff.setVisibility(View.INVISIBLE);
                soccerOn.setVisibility(View.VISIBLE);
            }
        }
    }

    public void click_soccer(View view) {
        if(soccerOn.getVisibility()==View.VISIBLE){
            soccerOff.setVisibility(View.VISIBLE);
            soccerOn.setVisibility(View.INVISIBLE);
        }
        else {
            soccerOn.setVisibility(View.VISIBLE);
            soccerOff.setVisibility(View.INVISIBLE);
        }
        if(soccerOff.getVisibility()==View.VISIBLE){
            if(basketOff.getVisibility()==View.VISIBLE){
                basketOff.setVisibility(View.INVISIBLE);
                basketOn.setVisibility(View.VISIBLE);
            }
            else if(tennisOff.getVisibility()==View.VISIBLE) {
                tennisOff.setVisibility(View.INVISIBLE);
                tennisOn.setVisibility(View.VISIBLE);
            }
        }
    }

    public void click_tennis(View view) {
        if(tennisOn.getVisibility()==View.VISIBLE){
            tennisOff.setVisibility(View.VISIBLE);
            tennisOn.setVisibility(View.INVISIBLE);
        }
        else {
            tennisOn.setVisibility(View.VISIBLE);
            tennisOff.setVisibility(View.INVISIBLE);
        }
        if(tennisOff.getVisibility()==View.VISIBLE){
            if(basketOff.getVisibility()==View.VISIBLE){
                basketOff.setVisibility(View.INVISIBLE);
                basketOn.setVisibility(View.VISIBLE);
            }
            else if(soccerOff.getVisibility()==View.VISIBLE) {
                soccerOff.setVisibility(View.INVISIBLE);
                soccerOn.setVisibility(View.VISIBLE);
            }
        }
    }
}
