package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by marku on 09.05.2017.
 */

@RunWith(AndroidJUnit4.class)
public class CategoriesInstrumentedTest {

    private DBHandler dbh;
    private DBEntry dbc;

    @Rule
    public ActivityTestRule<CategoriesActivity> mActivityRule = new ActivityTestRule<>(CategoriesActivity.class, false, false);

    @Before
    public void setup(){
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbc = new DBEntry(context);
        TestHelper.initCategories(context);
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown(){
        dbh.close();
        dbc.close();
    }

    @Test
    public void checkIfComponentsAreDisplayed() {
        onView(withId(R.id.cat_input_txt)).check(matches(isDisplayed()));
        onView(withId(R.id.cat_add_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.cat_subcat_label_list)).check(matches(isDisplayed()));
        onView(withId(R.id.cat_subcat_label)).check(matches(isDisplayed()));
        onView(withId(R.id.cat_maincat_spinner)).check(matches(isDisplayed()));
        onView(withId(R.id.cat_maincat_label)).check(matches(isDisplayed()));
        onView(withId(R.id.cat_listview)).check(matches(not(isDisplayed())));
    }

    @Test
    public void checkIfInputTextIsEmpty() {
        onView(withId(R.id.cat_input_txt)).check(ViewAssertions.matches(withText("")));
    }

    @Test
    public void shouldChooseMainCatAndShowSubCat() {
        onView(withId(R.id.cat_maincat_spinner)).perform(click());
        String selectionText = "Sport";
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(allOf(withId(R.id.cat_subcat_label_list), withText("All subcategories of " + selectionText))).check(matches(isDisplayed()));
        onView(allOf(withText("Walking"), withParent(withId(R.id.cat_listview)))).check(matches(isDisplayed()));
        onView(allOf(withText("Running"), withParent(withId(R.id.cat_listview)))).check(matches(isDisplayed()));
        onView(allOf(withText("Climbing"), withParent(withId(R.id.cat_listview)))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldChangeEditText(){
        onView(withId(R.id.cat_input_txt)).perform(clearText(), typeText("Some Text"));
        Espresso.closeSoftKeyboard();
        onView(allOf(withId(R.id.cat_input_txt), withText("Some Text"))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldAddASubcategory(){
        String text = "Kreuzheben";
        onView(withId(R.id.cat_maincat_spinner)).perform(click());
        String selectionText = "Sport";
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(withId(R.id.cat_input_txt)).perform(clearText(), typeText(text));
        onView(withId(R.id.cat_add_btn)).perform(click());
        Espresso.closeSoftKeyboard();
        onView(allOf(withText(text), withParent(withId(R.id.cat_listview)))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldOpenAlertDialog(){
        onView(withId(R.id.cat_maincat_spinner)).perform(click());
        String selectionText = "Sport";
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(allOf(withText("Walking"), withParent(withId(R.id.cat_listview)))).perform(click());
        onView(withText("Edit your subcategory.")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldCancelAlertDialog(){
        onView(withId(R.id.cat_maincat_spinner)).perform(click());
        String selectionText = "Sport";
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(allOf(withText("Walking"), withParent(withId(R.id.cat_listview)))).perform(click());
        onView(withText("Cancel")).perform(click());
        onView(withText("Edit your subcategory.")).check(doesNotExist());
    }

    @Test
    public void alertDialogShouldUpdateCat(){
        String text = "Reiten";
        onView(withId(R.id.cat_maincat_spinner)).perform(click());
        String selectionText = "Sport";
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(allOf(withText("Walking"), withParent(withId(R.id.cat_listview)))).perform(click());
        onView(allOf(withClassName(endsWith("EditText")))).perform(clearText(), typeText(text));
        Espresso.closeSoftKeyboard();
        onView(withText(text)).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        onView(allOf(withText(text), withParent(withId(R.id.cat_listview)))).check(matches(isDisplayed()));
    }

    @Test
    public void alertDialogShouldDeleteCat(){
        onView(withId(R.id.cat_maincat_spinner)).perform(click());
        String selectionText = "Sport";
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(allOf(withText("Walking"), withParent(withId(R.id.cat_listview)))).check(matches(isDisplayed()));
        onView(allOf(withText("Walking"), withParent(withId(R.id.cat_listview)))).perform(click());
        onView(withText("Delete")).perform(click());
        onView(allOf(withText("Walking"), withParent(withId(R.id.cat_listview)))).check(doesNotExist());
    }



}
