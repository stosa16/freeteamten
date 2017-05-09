package at.sw2017.pocketdiary;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
public class SettingScreenInstrumentedTest {

    private String titleToBeTyped;

    @Rule
    public ActivityTestRule<SettingScreen> mActivityRule = new ActivityTestRule<SettingScreen>(SettingScreen.class);

    @Test
    public void testButtons() throws Exception {
        onView(withId(R.id.editCategories)).check(matches(isClickable()));
        onView(withId(R.id.editFriends)).check(matches(isClickable()));
        onView(withId(R.id.cancel)).check(matches(isClickable()));
        onView(withId(R.id.save)).check(matches(isClickable()));
    }

    @Test
    public void testSwitch() throws Exception {
        onView(withId(R.id.switch1)).check(matches(not(isChecked())));
        onView(withId(R.id.switch1)).perform(click());
        onView(withId(R.id.switch1)).check(matches(isChecked()));
    }

    @Test
    public void testInputField(){
        onView(withId(R.id.User_name_id)).perform(typeText("user1"), closeSoftKeyboard());
        onView(withId(R.id.current_pin)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.new_pin1)).perform(typeText("def"), closeSoftKeyboard());
        onView(withId(R.id.new_pin2)).perform(typeText("ghi"), closeSoftKeyboard());

        onView(withText("user1")).check(matches(isDisplayed()));
        onView(withText("abc")).check(matches(isDisplayed()));
        onView(withText("def")).check(matches(isDisplayed()));
        onView(withText("ghi")).check(matches(isDisplayed()));
    }


    @Test
    public void testPINCorrect() throws Exception {
        onView(withId(R.id.User_name_id)).perform(typeText("user1"), closeSoftKeyboard());
        DBUserSetting dbUserSetting = new DBUserSetting(mActivityRule.getActivity());
        List<UserSetting> temp = dbUserSetting.getUserSetting(1);
        UserSetting userSetting = temp.get(0);

        final EditText user_name = (EditText) mActivityRule.getActivity().findViewById(R.id.User_name_id);
        String name = user_name.getText().toString();

        checkInput.check_user_name(name, userSetting, user_name);
        assertEquals("user1", userSetting.getUserName());

        userSetting.setPin("a");
        String saved_pin = userSetting.getPin();

        onView(withId(R.id.current_pin)).perform(typeText("b"), closeSoftKeyboard());
        onView(withId(R.id.new_pin1)).perform(typeText("def"), closeSoftKeyboard());
        onView(withId(R.id.new_pin2)).perform(typeText("def"), closeSoftKeyboard());

        EditText old_pin = (EditText) mActivityRule.getActivity().findViewById(R.id.current_pin);
        EditText new_ = (EditText) mActivityRule.getActivity().findViewById(R.id.new_pin1);
        EditText repeat_ = (EditText) mActivityRule.getActivity().findViewById(R.id.new_pin2);
        boolean ret = true;
        ret = checkInput.check_old_pin(old_pin.getText().toString(), saved_pin, new_.getText().toString(), repeat_.getText().toString());
        assertEquals(ret, false);

        onView(withId(R.id.current_pin)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.current_pin)).perform(clearText());
        old_pin = (EditText) mActivityRule.getActivity().findViewById(R.id.current_pin);

        ret = checkInput.check_old_pin(old_pin.getText().toString(), saved_pin, new_.getText().toString(), repeat_.getText().toString());
        assertEquals(ret, false);

        onView(withId(R.id.current_pin)).perform(typeText("a"), closeSoftKeyboard());
        old_pin = (EditText) mActivityRule.getActivity().findViewById(R.id.current_pin);

        ret = checkInput.check_old_pin(old_pin.getText().toString(), saved_pin, new_.getText().toString(), repeat_.getText().toString());
        assertEquals(ret, true);

        onView(withId(R.id.new_pin2)).perform(clearText());
        onView(withId(R.id.new_pin2)).perform(typeText("de"), closeSoftKeyboard());
        new_ = (EditText) mActivityRule.getActivity().findViewById(R.id.new_pin1);
        repeat_ = (EditText) mActivityRule.getActivity().findViewById(R.id.new_pin2);

        ret = checkInput.check_new_pin(new_.getText().toString(), repeat_.getText().toString(), userSetting);
        assertEquals(ret, false);

        onView(withId(R.id.new_pin2)).perform(clearText());
        onView(withId(R.id.new_pin2)).perform(typeText("def"), closeSoftKeyboard());
        new_ = (EditText) mActivityRule.getActivity().findViewById(R.id.new_pin1);
        repeat_ = (EditText) mActivityRule.getActivity().findViewById(R.id.new_pin2);
        ret = checkInput.check_new_pin(new_.getText().toString(), repeat_.getText().toString(), userSetting);
        assertEquals(ret, true);
        assertEquals("def", userSetting.getPin());
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
    public void pressSaveButton() {
        DBUserSetting dbUserSetting = new DBUserSetting(mActivityRule.getActivity());
        List<UserSetting> temp = dbUserSetting.getUserSetting(1);
        UserSetting userSetting = temp.get(0);
        userSetting.setPin("a");
        dbUserSetting.update(userSetting, 1);
        onView(withId(R.id.current_pin)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.new_pin1)).perform(typeText("b"), closeSoftKeyboard());
        onView(withId(R.id.new_pin2)).perform(typeText("b"), closeSoftKeyboard());
        onView(withId(R.id.save)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }

    @Test
    public void pressCancelButton() {
        onView(withId(R.id.cancel)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }
}
