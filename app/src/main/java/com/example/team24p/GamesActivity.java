package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GamesActivity extends AppCompatActivity {
    EditText selectDate,selectTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String userNameLoggedIn;
    private CalendarView calendarView;



    private  ListView myAct;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef ;
    private ArrayList<String> items;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        final String markerName = getIntent().getStringExtra("markerName");
        userNameLoggedIn= getIntent().getStringExtra("userNameLoggedIn");
        calendarView = (CalendarView)findViewById(R.id.cGames);
        final Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        calendarView.setMinDate(System.currentTimeMillis()-1000);

        ImageButton button = (ImageButton) findViewById(R.id.rateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stRate = new Intent(getApplicationContext(), RatingActivity.class);
                stRate.putExtra("userNameLoggedIn", userNameLoggedIn);
                stRate.putExtra("markerName", markerName);
                startActivity(stRate);
            }
        });

        ImageButton button2 = (ImageButton) findViewById(R.id.defectsButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stDef = new Intent(getApplicationContext(), defectReportActivity.class);
                stDef.putExtra("userNameLoggedIn", userNameLoggedIn);
                stDef.putExtra("markerName", markerName);
                startActivity(stDef);
            }
        });



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Intent inten = getIntent();
                finish();
                startActivity(inten);

                Intent intent = new Intent(GamesActivity.this,DayActivity.class);
                String day,months,years;
                day = String.valueOf(dayOfMonth);
                months = String.valueOf(month+1);
                years = String.valueOf(year);
                intent.putExtra("dayOfMonth", day);
                intent.putExtra("month", months);
                intent.putExtra("year", years);
                intent.putExtra("markerName", markerName);
                intent.putExtra("userNameLoggedIn", userNameLoggedIn);

                startActivity(intent);

            }
        });


        myAct = (ListView)findViewById(R.id.groundDefect);
        items = new ArrayList<>();
        items.clear();
        myAct.setAdapter(null);


        mRef = mDatabase.getReference().child("Defects");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object > defects = (HashMap<String, Object>)  dataSnapshot.getValue();
                String x="";
                int flag69=0;
                Object[] keysets =  defects.keySet().toArray();
                for (Object key : defects.values()) {
                    Map<String, Object> singleDefect = (Map<String, Object>) key;
                  //  for (Object key2 : singleDefect.keySet()) {
                        if((singleDefect.get("ground").toString().equals(markerName))&&(singleDefect.get("fixed").toString().equals("false"))){
                             x = "Description of the Defect: " +
                                     singleDefect.get("description").toString() + " - " +
                                    singleDefect.get("ground").toString() + " - " +
                                    "Opened: " + singleDefect.get("date").toString() + " " + singleDefect.get("hour").toString();
                            items.add(x);
                            flag69=1;
                    }


               //for }

                }
                if (flag69==0){
                    x="There is no Active Defects on This Ground right Now :-)";
                    items.add(x);}
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
                myAct.setAdapter(null);
                myAct.setAdapter(adapter);


            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) myAct.getItemAtPosition(position);
                String x[] = str.split(" - ",3);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Intent appInfo = new Intent(getApplicationContext(), UsersInGame.class);
               // appInfo.putExtra("userNameLoggedIn",username);
                appInfo.putExtra("defects",(Serializable)items);


                startActivity(appInfo);
            }
        });

    }

}