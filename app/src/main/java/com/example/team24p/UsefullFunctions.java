package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.os.Build;
import android.util.Patterns;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class UsefullFunctions {
    private static int flag=0;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    static boolean date;

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
    public String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
    public String defectExist(String key){
        mRef = mDatabase.getReference().child("Defects").child(key).child("date");
        return mRef.getKey();
    }
    public String eventExist(){
        mRef = mDatabase.getReference().child("Events");
        return mRef.getKey();
    }
    public String usersExist(){
        mRef = mDatabase.getReference().child("Users");
        return mRef.getKey();
    }
    public String ratingExist(){
        mRef = mDatabase.getReference().child("rating");
        return mRef.getKey();
    }

}
