package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

/**
 * Created by marku on 09.05.2017.
 */

@RunWith(AndroidJUnit4.class)
public class ReviewInstrumentedTest {

    private DBHandler dbh;
    private DBEntry dbe;

    @Rule
    public ActivityTestRule<ReviewActivity> mActivityRule = new ActivityTestRule<>(ReviewActivity.class, false, false);

    @Before
    public void setup(){
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbe = new DBEntry(context);
        TestHelper.initCategories(context);
        createTestData();
        Intents.init();

    }

    @After
    public void tearDown(){
        dbh.close();
        dbe.close();
        Intents.release();
    }

    @Test
    public void checkIfComponentsAreDisplayed() {
        mActivityRule.launchActivity(new Intent());
        onView(withId(R.id.review_cal_view)).check(matches(isDisplayed()));
        onView(withId(R.id.review_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldListEntryOfCurrentDay() {
        mActivityRule.launchActivity(new Intent());
        onView(allOf(withText("Test title"), withParent(withId(R.id.review_list_view)))).check(matches(isDisplayed()));
    }

    @Test
    public void pressListItem() {
        mActivityRule.launchActivity(new Intent());
        onView(allOf(withText("Test title"), withParent(withId(R.id.review_list_view)))).perform(click());
        intended(hasComponent(ShowEntryScreen.class.getName()));
    }

    @Test
    public void callReviewActivityWithParameters() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, StatisticAnalysisActivity.class);
        ArrayList<String> entries_ids = new ArrayList<>();
        entries_ids.add("1");
        intent.putStringArrayListExtra("entries_ids", entries_ids);
        mActivityRule.launchActivity(intent);

        onView(allOf(withText("Test title"), withParent(withId(R.id.review_list_view)))).check(matches(isDisplayed()));
    }

    public void createTestData(){

        Entry entry = new Entry();
        entry.setTitle("Test title");
        entry.setMainCategoryId(1);
        entry.setSubCategoryId(2);
        entry.setDate((Date) Calendar.getInstance().getTime());
        entry.setDescription("This is a test description.");
        dbe.insert(entry);

        entry.setTitle("Another Day");
        entry.setMainCategoryId(1);
        entry.setSubCategoryId(2);
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        entry.setDate(dt);
        entry.setDescription("This is a test description.");
        dbe.insert(entry);
    }

}
