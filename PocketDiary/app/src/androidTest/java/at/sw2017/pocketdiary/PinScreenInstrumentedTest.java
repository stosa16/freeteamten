package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PinScreenInstrumentedTest {

    private PinScreen activity;
    private String titleToBeTyped;
    public DBHandler dbh;

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
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        TestHelper.createUserSettingEmpty(context);
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
        userSetting.setPin("aaaaa");
        dbUserSetting.update(userSetting, 1);
        onView(withId(R.id.editText)).perform(typeText("aaaa"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());
        intended(hasComponent(PinScreen.class.getName()));
        onView(withId(R.id.editText)).perform(clearText());
        onView(withId(R.id.editText)).perform(typeText("aaaaa"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }
}
