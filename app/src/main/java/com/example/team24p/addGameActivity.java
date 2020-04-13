package com.example.team24p;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.autofill.AutofillValue;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addGameActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");
    private String date,hour,ground,username;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        date = getIntent().getStringExtra("date");
        ground = getIntent().getStringExtra("markerName");
        username = getIntent().getStringExtra("userNameLoggedIn");


        TextInputEditText hourText = (TextInputEditText)findViewById(R.id.hourInput);
        TextInputEditText dateText = (TextInputEditText)findViewById(R.id.dateInput);
        TextInputEditText groundText = (TextInputEditText)findViewById(R.id.groundInput);


        dateText.autofill(AutofillValue.forText(date));
        groundText.autofill(AutofillValue.forText(ground));


        Spinner numOfParticipants = findViewById(R.id.maxPart);
        String [] items = new String[]{"1","2","3","4","5","6","7","8","9","10+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,items);
        numOfParticipants.setAdapter(adapter);

        String text = numOfParticipants.getSelectedItem().toString();
        ArrayList<String> users = new ArrayList<>();
        Map<String,Object> games = new HashMap<String,Object>();
        games.put("date",date);
        games.put("ground",ground);
        games.put("hour",hour);
        games.put("userlist",users);

        mRef.push().updateChildren(games);






    }
}
