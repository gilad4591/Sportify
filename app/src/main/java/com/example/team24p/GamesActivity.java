package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GamesActivity extends AppCompatActivity {
    //Games Activity variables
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
        // View Class Layout
        setContentView(R.layout.activity_games);
        final String markerName = getIntent().getStringExtra("markerName");
        //Gets Details From Maps Activity Class
        userNameLoggedIn= getIntent().getStringExtra("userNameLoggedIn");
        calendarView = (CalendarView)findViewById(R.id.cGames); //Showing Calendar View
        final Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        calendarView.setMinDate(System.currentTimeMillis()-1000);


        //Rate Ground Button on Screen
        ImageButton button = (ImageButton) findViewById(R.id.rateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //Pass Grounds name to Rating Activity Class
                Intent stRate = new Intent(getApplicationContext(), RatingActivity.class);
                stRate.putExtra("userNameLoggedIn", userNameLoggedIn);
                stRate.putExtra("markerName", markerName);
                startActivity(stRate);
            }
        });

        //Grounds Defect Button on Screen
        ImageButton button2 = (ImageButton) findViewById(R.id.defectsButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //Pass Grounds name to Defects report Activity Class
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
                //Whene Click DAY on Calender View Send Details To Day Activity Class
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

        // Store And Showing DEFECTs From The Data Base
        mRef = mDatabase.getReference().child("Defects");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {      //Store Defects In HashMap
                Map<String, Object> defects = (HashMap<String, Object>) dataSnapshot.getValue();
                String x = "";
                int flag69 = 0; // This Flag69 Is Used For Know If The specific This Ground has Defects already.
                final ArrayList<String> keys = new ArrayList<>();
                final Object[] keysets = defects.keySet().toArray();
                int i = 0;
                for (Object key : defects.values()) {
                    Map<String, Object> singleDefect = (Map<String, Object>) key;
                    // If This Ground Profile In DB ' fixed=False turn on the flag69
                    //  for (Object key2 : singleDefect.keySet())
                    // Show the defects Details IN GOOD STRING
                    if ((singleDefect.get("ground").toString().equals(markerName)) && (singleDefect.get("fixed").toString().equals("false"))) {
                        x = "תיאור: " +
                                singleDefect.get("description").toString() + " - " + "\n" +
                                singleDefect.get("ground").toString() + " - " +
                                "נפתח ב: " + singleDefect.get("date").toString() + " " + singleDefect.get("hour").toString();
                        items.add(x);
                        keys.add(keysets[i].toString());
                        flag69 = 1;
                    }
                    i++;

                    //for }

                }
                // If the Defects flag not turn on.
                if (flag69 == 0) {
                    x = "אין ליקויים במגרש כרגע :)";
                    items.add(x);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                myAct.setAdapter(null);
                myAct.setAdapter(adapter);


                //There is active Defects on this fround
                if (flag69 != 0) {
                    myAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String str = (String) myAct.getItemAtPosition(position);
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);  //Send the Defect Key to Defects Activity Class
                            Intent appInfo = new Intent(getApplicationContext(), defectDetailsActivity.class);
                            appInfo.putExtra("userNameLoggedIn", userNameLoggedIn);
                            appInfo.putExtra("defectKey", keys.get(position));

                            startActivity(appInfo);
                        }

                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}