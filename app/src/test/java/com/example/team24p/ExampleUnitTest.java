package com.example.team24p;

import org.junit.Test;
import android.content.Context;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    UsefullFunctions us = new UsefullFunctions();

    @Test
    public void useAppContext() throws ParseException {
    }

    @Test
    public void testTime(){
        assertEquals(true,us.checkTime("18:00"));
    }
    @Test
    public void testTimeIncorrect(){
        assertEquals(false,us.checkTime("1T8:00"));
    }
    @Test
    public void testTimeIncorrect2(){
        assertEquals(false,us.checkTime("18800"));
    }
    @Test
    public void testTimeIncorrect3(){
        assertEquals(false,us.checkTime("18::00"));
    }
    @Test
    public void testTimeIncorrect4(){
        assertEquals(false,us.checkTime("10.00"));
    }
    @Test
    public void testRandomColor(){
        assertNotEquals("#FFFFFF",us.getRandomColor());
    }

    @Test
    public void testEmailCheck(){
        assertEquals(true,us.checkCorrectEmail("name@email.com"));
    }
    @Test
    public void testEmailCheck2(){
        assertEquals(false,us.checkCorrectEmail("name@@email.com"));
    }
    @Test
    public void testEmailCheck3(){
        assertEquals(false,us.checkCorrectEmail("@email.com"));
    }
    @Test
    public void testEmailCheck4(){
        assertEquals(false,us.checkCorrectEmail("a@email/com"));
    }
    @Test
    public void testEmailCheck5(){
        assertEquals(true,us.checkCorrectEmail("NAME@EMAIL.coM"));
    }
    @Test
    public void testPhoneNumber1(){assertEquals(true,us.checkCorrectPhoneNumber("0524565541"));}
    @Test
    public void testPhoneNumber2(){assertEquals(false,us.checkCorrectPhoneNumber("05245655"));}
    @Test
    public void testPhoneNumber3(){assertEquals(false,us.checkCorrectPhoneNumber("056636445a"));}
    @Test
    public void testPhoneNumber4(){assertEquals(false,us.checkCorrectPhoneNumber("1234567"));}
    @Test
    public void testPhoneNumber5(){assertEquals(false,us.checkCorrectPhoneNumber("ahfhejd"));}
    @Test
    public void testid1(){assertEquals(true,us.checkCorrectId("203722334"));}
    @Test
    public void testid2(){assertEquals(false,us.checkCorrectId("47589"));}
    @Test
    public void testid3(){assertEquals(false,us.checkCorrectId("20377333a"));}
    @Test
    public void testid4(){assertEquals(false,us.checkCorrectId("203722@34"));}
    @Test
    public void testid5(){assertEquals(false,us.checkCorrectId("20372232234"));}

    @Test
    public void testCheckDate() {
        Date d = new Date();
        d.setYear(2020);
        d.setMonth(4);
        d.setDate(20);
        try {
            assertEquals(true, us.checkDate("24/10/2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}