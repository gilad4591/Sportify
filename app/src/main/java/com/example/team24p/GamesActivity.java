package com.example.team24p;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class GamesActivity extends AppCompatActivity {
    EditText selectDate,selectTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String userNameLoggedIn;
    private CalendarView calendarView;

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

    }

}