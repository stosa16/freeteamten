package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBStatistic;

import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class CreateStatisticInstrumentedTest {

    DBHandler dbh;
    DBStatistic dbs;

    @Rule
    public ActivityTestRule<CreateStatisticActivity> mActivityRule = new ActivityTestRule<>(CreateStatisticActivity.class, false, false);

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbs = new DBStatistic(context);
        TestHelper.initCategories(context);
        TestHelper.initFriends(context);
        Intents.init();
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() {
        dbh.close();
        dbs.close();
        Intents.release();
    }

    @Test
    public void checkIfComponentsAreDisplayed() {
        onView(withId(R.id.create_stats_lbl_title)).check(matches(isDisplayed()));
        onView(withId(R.id.create_stats_inp_title)).check(matches(isDisplayed()));

        onView(withId(R.id.create_stats_lbl_from_date)).check(matches(isDisplayed()));
        onView(withId(R.id.create_stats_inp_from)).check(matches(isDisplayed()));

        onView(withId(R.id.create_stats_lbl_til_date)).check(matches(isDisplayed()));
        onView(withId(R.id.create_stats_inp_til)).check(matches(isDisplayed()));

        onView(withId(R.id.create_stats_lbl_main_cat)).check(matches(isDisplayed()));
        onView(withId(R.id.create_stats_spin_main_cat)).check(matches(isDisplayed()));

        onView(withId(R.id.create_stats_lbl_sub_cat)).check(matches(isDisplayed()));
        onView(withId(R.id.create_stats_spin_sub_cat)).check(matches(isDisplayed()));

        onView(withId(R.id.create_stats_lbl_term)).check(matches(isDisplayed()));
        onView(withId(R.id.create_stats_inp_term)).check(matches(isDisplayed()));

        onView(withId(R.id.create_stats_btn_cancel)).check(matches(isDisplayed()));
        onView(withId(R.id.create_stats_btn_save)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldOpenDatePickerFromOnClick() {
        onView(withId(R.id.create_stats_inp_from)).perform(click());
        onView(allOf(withClassName(endsWith("DatePicker")))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldOpenDatePickerTilOnClick() {
        onView(withId(R.id.create_stats_inp_til)).perform(click());
        onView(allOf(withClassName(endsWith("DatePicker")))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldChooseADateFrom() {
        onView(withId(R.id.create_stats_inp_from)).perform(click());
        onView(allOf(withClassName(endsWith("DatePicker")))).perform(PickerActions.setDate(2017, 5, 30));
        onView(allOf(withText("OK"))).perform(click());
        onView(allOf(withId(R.id.create_stats_inp_from), withText("30.5.2017"))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldChooseADateUntil() {
        onView(withId(R.id.create_stats_inp_til)).perform(click());
        onView(allOf(withClassName(endsWith("DatePicker")))).perform(PickerActions.setDate(2017, 5, 30));
        onView(allOf(withText("OK"))).perform(click());
        onView(allOf(withId(R.id.create_stats_inp_til), withText("30.5.2017"))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldInsertTitle() {
        String test_text = "Some Title";
        onView(withId(R.id.create_stats_inp_title)).perform(clearText(), typeText(test_text));
        Espresso.closeSoftKeyboard();
        onView(allOf(withId(R.id.create_stats_inp_title), withText(test_text))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldInsertTerm() {
        String test_text = "Some Term";
        onView(withId(R.id.create_stats_inp_term)).perform(clearText(), typeText(test_text));
        Espresso.closeSoftKeyboard();
        onView(allOf(withId(R.id.create_stats_inp_term), withText(test_text))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldChooseMainCategory() {
        String selectionText = "Sport";
        onView(withId(R.id.create_stats_spin_main_cat)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(withId(R.id.create_stats_spin_main_cat)).check(matches(withSpinnerText(containsString(selectionText))));
    }

    @Test
    public void shouldChooseMainCatAndSubCat() {
        String selectionTextMain = "Sport";
        onView(withId(R.id.create_stats_spin_main_cat)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(selectionTextMain))).perform(click());

        String selectionTextSub = "Walking";
        onView(withId(R.id.create_stats_spin_sub_cat)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(selectionTextSub))).perform(click());

        onView(withId(R.id.create_stats_spin_main_cat)).check(matches(withSpinnerText(containsString(selectionTextMain))));
        onView(withId(R.id.create_stats_spin_sub_cat)).check(matches(withSpinnerText(containsString(selectionTextSub))));
    }

    @Test
    public void shouldWarnUserIfNoTitle() {
        SystemClock.sleep(3000);
        onView(withId(R.id.create_stats_btn_save)).perform(click());
        onView(withText("Provide a valid title.")).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void shouldWarnUserIfNoSecArg() {
        SystemClock.sleep(3000);
        String text = "Some Title";
        onView(withId(R.id.create_stats_inp_title)).perform(clearText(), typeText(text));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.create_stats_btn_save)).perform(click());
        onView(withText("Please provide at least on argument additional to the title.")).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void shouldCreateStatisticWithTwoArgs() {
        String title = "Some Title";
        String term = "Some Term";

        onView(withId(R.id.create_stats_inp_title)).perform(clearText(), typeText(title));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.create_stats_inp_term)).perform(clearText(), typeText(term));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.create_stats_btn_save)).perform(click());
        intended(hasComponent(StatisticScreenActivity.class.getName()));
    }

    @Test
    public void shouldCreateStatisticWithAllArgs() {
        String title = "Some Title";
        String term = "Some Term";
        String main_cat = "Sport";
        String sub_cat = "Walking";

        onView(withId(R.id.create_stats_inp_title)).perform(clearText(), typeText(title));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.create_stats_inp_from)).perform(click());
        onView(allOf(withClassName(endsWith("DatePicker")))).perform(PickerActions.setDate(2017, 5, 30));
        onView(allOf(withText("OK"))).perform(click());

        onView(withId(R.id.create_stats_inp_til)).perform(click());
        onView(allOf(withClassName(endsWith("DatePicker")))).perform(PickerActions.setDate(2017, 5, 30));
        onView(allOf(withText("OK"))).perform(click());

        onView(withId(R.id.create_stats_spin_main_cat)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(main_cat))).perform(click());

        onView(withId(R.id.create_stats_spin_sub_cat)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(sub_cat))).perform(click());


        onView(withId(R.id.create_stats_inp_term)).perform(clearText(), typeText(term));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.create_stats_btn_save)).perform(click());
        intended(hasComponent(StatisticScreenActivity.class.getName()));
    }

    @Test
    public void cancelShouldLeadToStatisticScreen() {
        onView(withId(R.id.create_stats_btn_cancel)).perform(click());
        intended(hasComponent(StatisticScreenActivity.class.getName()));
    }
}