package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class defectReportActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef;
    private String email ;
    private String nameS;
    private String groundS;
    private String hourS;
    private String dateS;
    private TextView name;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_report);
        email = getIntent().getStringExtra("userNameLoggedIn");

        Button rep = (Button)findViewById(R.id.repButton);
        rep.setVisibility(View.INVISIBLE);

        groundS = getIntent().getStringExtra("markerName");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/M/yyyy");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm");
        hourS = dtf2.format(now);
        dateS = dtf.format(now);

        TextView hour = (TextView)findViewById(R.id.hourTextDef);
        TextView date = (TextView)findViewById(R.id.dateTextDef);
        name = (TextView)findViewById(R.id.nameTextDef);
        final TextView desc = (TextView)findViewById(R.id.descTextDef);
        TextView ground = (TextView)findViewById(R.id.groundTextDef);

        hour.setText(hourS);
        date.setText(dateS);
        ground.setText(groundS);

        //get the user that logged in details
        mRef = mDatabase.getReference().child("Users");
        if(email!=null) {
            rep.setVisibility(View.VISIBLE);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : usersList.keySet()) {
                        Map<String, Object> value = (HashMap<String, Object>) usersList.get(key);
                        if (email.equals(value.get("username").toString())) {
                            nameS = value.get("Name").toString();
                        }
                    }
                    name.setText(nameS);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(this,"You are not logged in. \n log in before sending a defect report" , Toast.LENGTH_SHORT).show();
        }
        mRef = mDatabase.getReference().child("Defects");
        //if user decide to report and click the report button it set the details to firebase
        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(desc.getText().toString().trim()==null){
                    Toast.makeText(defectReportActivity.this,"fill the description before you press the button" , Toast.LENGTH_SHORT).show();
                }
                else {
                    Map<String, Object> def = new HashMap<String, Object>();
                    def.put("date", dateS);
                    def.put("description", desc.getText().toString().trim());
                    def.put("fixed", false);
                    def.put("ground", groundS);
                    def.put("hour", hourS);
                    def.put("userReports", email);
                    mRef.push().updateChildren(def);
                    Intent intent = new Intent(defectReportActivity.this,GamesActivity.class); //move to games activity after that
                    intent.putExtra("userNameLoggedIn",email);
                    intent.putExtra("markerName",groundS);
                    startActivity(intent);

                    Toast.makeText(defectReportActivity.this,"The report was sent to the admins" , Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
