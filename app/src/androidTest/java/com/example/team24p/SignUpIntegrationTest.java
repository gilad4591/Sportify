package com.example.team24p;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SignUpIntegrationTest {
    @Rule
    public ActivityTestRule<SignUpActivity> main = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void signUpCheck(){
        Espresso.onView(withId(R.id.SignInButton)).check(matches(isClickable()));
        Espresso.onView(withId(R.id.RegisterButton)).check(matches(isClickable()));
        Espresso.onView(withId(R.id.EmailTextView)).check(matches(withHint("Email")));
        Espresso.onView(withId(R.id.ValidatePasswordEditText)).check(matches(withHint("Validate Password")));
        Espresso.onView(withId(R.id.PasswordTextView)).check(matches(withHint("Password")));
        Espresso.onView(withId(R.id.PhoneTextView)).perform(typeText("0524567411"));
        Espresso.onView(withId(R.id.NameTextView)).perform(typeText("Gilad Cohen"));
    }
}
