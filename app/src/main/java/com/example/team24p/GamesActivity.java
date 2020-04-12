package com.example.team24p;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class GamesActivity extends AppCompatActivity {
    EditText selectDate,selectTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        final String markerName = getIntent().getStringExtra("markerName");

        calendarView = (CalendarView)findViewById(R.id.cGames);
        final Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        calendarView.setMinDate(System.currentTimeMillis()-1000);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Intent intent = new Intent(GamesActivity.this,DayActivity.class);
                String day,months,years;
                day = String.valueOf(dayOfMonth);
                months = String.valueOf(month+1);
                years = String.valueOf(year);
                intent.putExtra("dayOfMonth", day);
                intent.putExtra("month", months);
                intent.putExtra("year", years);
                intent.putExtra("markerName", markerName);

                startActivity(intent);

            }
        });
    }

}