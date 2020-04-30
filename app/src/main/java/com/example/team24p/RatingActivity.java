package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {

    private String MarkerName;
    private String UserName;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("rating");;
    private Button button;
    private RatingBar ratingBar;
    private Button backButton;
    private int flag;
    private double sum=0.0;
    private int counter=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        MarkerName = getIntent().getStringExtra("markerName");
        UserName =  getIntent().getStringExtra("userNameLoggedIn");
        ratingBar = findViewById(R.id.ratingBar);
        backButton = findViewById(R.id.buttonBack);
        final TextView rateUs = (TextView)findViewById(R.id.rateUs);
        flag = 0;

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sum=0;
                counter=0;
                Map<String, Object > ratings = (HashMap<String, Object>)  dataSnapshot.getValue();
                    for (String key : ratings.keySet()) {
                        Map<String, Object > value = (HashMap<String, Object>)ratings.get(key);

                            if (value.get("username").toString().equals(UserName) && value.get("ground").toString().equals(MarkerName)) flag = 1; // user already voted
                        if(value.get("ground").toString().equals(MarkerName)) {
                            counter++;
                            sum += Double.parseDouble(value.get("rating").toString());
                        }
                    }


                if(flag==1){
                    ratingBar.setVisibility(View.INVISIBLE);
                    rateUs.setVisibility(View.INVISIBLE);
                }

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        int r = (int) v;
                        String message=null;
                        if((UserName!=null) && (flag!=1)){
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

                            Map<String, Object > rate = new HashMap<String, Object>();
                            rate.put("ground", MarkerName);
                            rate.put("rating", String.valueOf(v));
                            rate.put("username", UserName);
                            if(String.valueOf(v)!="0") mRef.push().updateChildren(rate);
                        }
                        else if(flag==1)Toast.makeText(RatingActivity.this, "You already voted for this ground" , Toast.LENGTH_SHORT).show();
                        else Toast.makeText(RatingActivity.this, "You are not logged in. \n log in before rate" , Toast.LENGTH_SHORT).show();
                    }
                });
                double avg = 0;
                avg = sum/counter;

                TextView avgTextView = (TextView)findViewById(R.id.avgRating);

                DecimalFormat ff = new DecimalFormat("#0.00");

                avgTextView.setText(ff.format(avg)+"/"+5);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RatingActivity.this,MapsActivity.class));
                finish();
            }
        });

    }

}

