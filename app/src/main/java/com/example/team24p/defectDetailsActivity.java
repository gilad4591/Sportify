package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class defectDetailsActivity extends AppCompatActivity {
    public String defectKey;
    private String userNameLoggedIn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef ;
    public String date,hour,desc,ground,userRep;
    public TextView grounddef,userdef,hourdef,datedef,descdef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_details);
        defectKey = getIntent().getStringExtra("defectKey");
        userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
        mRef = mDatabase.getReference().child("UsersAndPasswords");
        descdef = (TextView)findViewById(R.id.descDef);
        datedef = (TextView)findViewById(R.id.dateDef);
        hourdef = (TextView)findViewById(R.id.hourDef);
        userdef = (TextView)findViewById(R.id.userRep);
        grounddef = (TextView)findViewById(R.id.groundDef);
        final Button fixB = (Button)findViewById(R.id.fixedBut);
        if(userNameLoggedIn==null)fixB.setVisibility(View.INVISIBLE);



        mRef.orderByChild("UserName").equalTo(userNameLoggedIn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : users.keySet()) {
                    Map<String, Object> user = (HashMap<String, Object>) users.get(key);
                    if (user.get("isAdmin").toString().equals("False"))
                        fixB.setVisibility(View.INVISIBLE);
                    getDefectDetails(defectKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fixB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef = mDatabase.getReference().child("Defects").child(defectKey).child("fixed");
                mRef.setValue(true);
                finish();
            }
        });



    }
    public void getDefectDetails(String defKey){
        mRef = mDatabase.getReference().child("Defects").child(defKey);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,String> def = (HashMap<String, String>) dataSnapshot.getValue();
                date = def.get("date");
                desc =def.get("description");
                ground = def.get("ground");
                hour = def.get("hour");
                userRep = def.get("userReports");

                descdef.setText(desc);
                datedef.setText(date);
                hourdef.setText(hour);
                userdef.setText(userRep);
                grounddef.setText(ground);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
