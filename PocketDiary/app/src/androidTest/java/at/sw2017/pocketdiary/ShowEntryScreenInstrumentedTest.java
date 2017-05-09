package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ShowEntryScreenInstrumentedTest {

    private DBHandler dbh;
    private Entry test_entry;
    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");

    @Rule
    public ActivityTestRule<ShowEntryScreen> mActivityRule = new ActivityTestRule<>(ShowEntryScreen.class, false, false);;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        TestHelper.initCategories(context);
        test_entry = TestHelper.createTestEntryBasic(context);
        Intent intent = new Intent();
        intent.putExtra("entry_id", Integer.toString((int) test_entry.getId()));
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void checkIfAllFieldsAreDisplayed() {
        onView(withId(R.id.out_title)).check(matches(isDisplayed()));
        onView(withId(R.id.out_description)).check(matches(isDisplayed()));
        onView(withId(R.id.out_category)).check(matches(isDisplayed()));
        onView(withId(R.id.out_subcategory)).check(matches(isDisplayed()));
        onView(withId(R.id.out_date)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfAllFieldsDisplayCorrect() {
        onView(withId(R.id.out_title)).check(matches(withText(test_entry.getTitle())));
        onView(withId(R.id.out_description)).check(matches(withText(test_entry.getDescription())));
        onView(withId(R.id.out_date)).check(matches(withText(date_format.format(test_entry.getDate()))));
        onView(withId(R.id.out_category)).check(matches(withText(test_entry.getMainCategory().getName())));
        onView(withId(R.id.out_subcategory)).check(matches(withText(test_entry.getSubCategory().getName())));
    }
}
