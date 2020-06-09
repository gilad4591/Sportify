package com.example.team24p;

import androidx.annotation.RequiresApi;
import android.os.Build;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class UsefullFunctions {
    private static int flag=0;
    static boolean date;

    public UsefullFunctions() {
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
        if (email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))
            return true;
        return false;
    }
    public String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
}
