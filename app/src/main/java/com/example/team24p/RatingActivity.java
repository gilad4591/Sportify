package com.example.team24p;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Rating;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class RatingActivity extends AppCompatActivity {
private String MarkerName;
private String UserName;
     Button button;
     RatingBar ratingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        MarkerName = getIntent().getStringExtra("markerName");
        UserName =  getIntent().getStringExtra("userNameLoggedIn");
        button = findViewById(R.id.button);
        ratingBar = findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int r = (int) v;
                String message=null;

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
            }
        });

    }

}

