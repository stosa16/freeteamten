package at.sw2017.pocketdiary;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class StartScreenInstrumentedTest {
    @Rule
    public ActivityTestRule<StartScreen> mActivityRule = new ActivityTestRule<StartScreen>(StartScreen.class);

    @Test
    public void testButtons() throws Exception {
        onView(withId(R.id.create_entry)).check(matches(isClickable()));
        onView(withId(R.id.review)).check(matches(isClickable()));
        onView(withId(R.id.statistic)).check(matches(isClickable()));
        onView(withId(R.id.settings)).check(matches(isClickable()));
    }
}
