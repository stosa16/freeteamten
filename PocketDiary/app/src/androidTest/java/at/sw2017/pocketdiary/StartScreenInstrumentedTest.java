package at.sw2017.pocketdiary;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class StartScreenInstrumentedTest {
    @Rule
    public ActivityTestRule<StartScreen> mActivityRule = new ActivityTestRule<StartScreen>(StartScreen.class);

    @Before
    public void setUp() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void release() {
        Intents.release();
    }


    @Test
    public void testButtons() throws Exception {
        onView(withId(R.id.create_entry)).check(matches(isClickable()));
        onView(withId(R.id.review)).check(matches(isClickable()));
        onView(withId(R.id.statistic)).check(matches(isClickable()));
        onView(withId(R.id.settings)).check(matches(isClickable()));
    }

    @Test
    public void pressCreateEntryButton() {
        onView(withId(R.id.create_entry)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void pressCreateReviewButton() {
        onView(withId(R.id.review)).perform(click());
        intended(hasComponent(ReviewActivity.class.getName()));
    }

    @Test
    public void pressSettingButton() {
        onView(withId(R.id.settings)).perform(click());
        intended(hasComponent(SettingScreen.class.getName()));
    }
}
