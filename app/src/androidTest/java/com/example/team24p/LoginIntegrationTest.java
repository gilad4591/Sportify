package com.example.team24p;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.util.Date;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class LoginIntegrationTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void LoginCheck(){
        Espresso.onView(withId(R.id.loginButton)).check(matches(isClickable()));
        Espresso.onView(withId(R.id.signUpButton)).check(matches(isClickable()));
        Espresso.onView(withId(R.id.EditTextEmail)).check(matches(withHint("Email")));
        Espresso.onView(withId(R.id.EditTextEmail)).perform(typeText("a@a.a"));
        Espresso.onView(withId(R.id.EditTextPassword)).perform(typeText("123456"));
        Espresso.onView(withId(R.id.loginButton)).perform(click());
    }
}
