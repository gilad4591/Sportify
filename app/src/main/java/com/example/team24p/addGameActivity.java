package com.example.team24p;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class addGameActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        getIntent().getStringExtra("date");
        getIntent().getStringExtra("groundName" );
        getIntent().getStringExtra("date" );
        getIntent().getStringExtra("userNameLoggedIn" );

        Spinner age = findViewById(R.id.spinner);
        String [] items = new String[]{"1","2","3","4","5","6","7","8","9","10+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,items);
        age.setAdapter(adapter);

    }
}
