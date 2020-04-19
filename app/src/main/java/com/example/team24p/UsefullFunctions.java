package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;




public class UsefullFunctions {
    private static int flag=0;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();

    public UsefullFunctions() {
    }

    public String checkUser(final String username){
        if(username!=null) {
            mRef = mDatabase.getReference().child("Users");
            mRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : usersList.keySet()) {
                        Map<String, Object> value = (HashMap<String, Object>) usersList.get(key);
                        if (value.get("username").toString().equals(username)) {
                            UsefullFunctions.flag=1;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
        }
        if (flag==1) return username;
        return "tamiryakov@gmail.com";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String changeDate(Date d){
        DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
        String newDate = dateFormat.format(d);

        return newDate;
    }

    public boolean checkDate(String date) throws ParseException {
        Date date1=null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        }catch (Exception e){}
        if (date1 != null)
            return true;
        return false;
    }
    public boolean checkTime(String time){
        if (!time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"))
            return false;
        return true;
    }
    public boolean checkCorrectEmail(String email){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return false;
        return true;
    }

}
