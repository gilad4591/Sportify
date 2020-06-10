package com.example.team24p;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class RatingActivityIntegrationTest {
    @Rule
    public ActivityTestRule<RatingActivity> rating = new ActivityTestRule<>(RatingActivity.class);

    @Test
    public void RatingCheck(){
        Espresso.onView(withId(R.id.ratingBar)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.buttonBack)).check(matches(isClickable()));
        Espresso.onView(withId(R.id.numOfRate)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.rateUs)).check(matches(isDisplayed()));

    }
}
