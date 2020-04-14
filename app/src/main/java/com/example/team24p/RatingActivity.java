package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {

    private String MarkerName;
    private String UserName;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("rating");
    private Button button;
    private RatingBar ratingBar;
    private int flag;
    private double sum=0.0;
    private int count=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        MarkerName = getIntent().getStringExtra("markerName");
        UserName =  getIntent().getStringExtra("userNameLoggedIn");
        button = findViewById(R.id.button);
        ratingBar = findViewById(R.id.ratingBar);
        flag = 0;

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             ArrayList<Map<String, String>> ratings = (ArrayList<Map<String, String>>) dataSnapshot.getValue();
              for (Map<String, String> entry : ratings) {
                  for (String key : entry.keySet()) {
                      String value = entry.get(key);
                      if (key.equals("username") && (key.equals("ground"))) {

                          if (value == UserName) flag = 1; // user already voted
                      }
                      if(key.equals("rating")){
                          count++;
                          sum+=Double.parseDouble(value);
                      }
                  }
              }
            }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });

        if(flag==1) ratingBar.setVisibility(View.INVISIBLE);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int r = (int) v;
                    String message=null;
                if(UserName!=null) {
                switch(r){
                    case 1: message="מתנצלים לשמוע!";
                        break;
                    case 2: message = "נפעל ע''מ שיפור השירות!";
                        break;
                    case 3: message = "תודה על תגובתך!";
                        break;
                    case 4: message = "תודה לך!";
                        break;
                    case 5: message = "יא מלך,מעריכים את זה!";
                        break;
                }
                Toast.makeText(RatingActivity.this, message , Toast.LENGTH_SHORT).show();

                    Map<String, Object> rate = new HashMap<String, Object>();
                    rate.put("ground", MarkerName);
                    rate.put("rating", r);
                    rate.put("username", UserName);
                    mRef.push().updateChildren(rate);
                }
                else Toast.makeText(RatingActivity.this, "You are not logged in. \n log in before rate" , Toast.LENGTH_SHORT).show();
            }
        });
        double avg = sum/count;
        TextView avgTextView = (TextView)findViewById(R.id.avgRating);
        avgTextView.setText(String.valueOf(avg)+"/"+5);

    }

}

