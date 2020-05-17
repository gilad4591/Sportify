package com.example.team24p;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws ParseException {
        // Context of the app under test.
        Date d = new Date();
        d.setYear(2020);
        d.setMonth(4);
        d.setDate(20);
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UsefullFunctions us = new UsefullFunctions();
        assertEquals("com.example.team24p", appContext.getPackageName());
        assertEquals("tamiryakov@gmail.com",us.checkUser("tamiryakov@gmail.com"));
        assertEquals(true, us.checkDate("24/10/2020"));
        assertEquals(true,us.checkTime("18:00"));
        assertEquals(true,us.checkCorrectEmail("gilad4591@gmail.com"));
        assertNotEquals("#FFFFFF",us.getRandomColor());
        assertEquals("date",us.defectExist("-M69lKbMD2BL5t46QTuU"));
        assertEquals("Events",us.eventExist());
    }
}
