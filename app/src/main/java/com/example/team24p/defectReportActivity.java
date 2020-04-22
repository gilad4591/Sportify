package com.example.team24p;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class defectReportActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("defects");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_report);
    }
}
