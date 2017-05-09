package at.sw2017.pocketdiary;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.util.concurrent.Runnables.doNothing;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PinScreenInstrumentedTest {

    private PinScreen activity;
    private String titleToBeTyped;

    @Rule
    public ActivityTestRule<PinScreen> mActivityRule = new ActivityTestRule<PinScreen>(PinScreen.class);

    @Test
    public void testButtons() throws Exception {
        onView(withId(R.id.button)).check(matches(isClickable()));
    }

    @Test
    public void testInputField(){
        onView(withId(R.id.editText)).perform(typeText("abc"), closeSoftKeyboard());

        onView(withText("abc")).check(matches(isDisplayed()));
    }

    @Before
    public void setUp() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());
        titleToBeTyped = "Running";
    }

    @After
    public void release() {
        Intents.release();
    }

    @Test
    public void pressLoginButton() {
        DBUserSetting dbUserSetting = new DBUserSetting(mActivityRule.getActivity());
        List<UserSetting> temp = dbUserSetting.getUserSetting(1);
        UserSetting userSetting = temp.get(0);
        userSetting.setPin("a");
        dbUserSetting.update(userSetting, 1);
        onView(withId(R.id.editText)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }
}
