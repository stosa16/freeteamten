package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ShowEntryScreenInstrumentedTest {

    private DBHandler dbh;
    private Entry test_entry;
    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;

    @Rule
    public ActivityTestRule<ShowEntryScreen> mActivityRule = new ActivityTestRule<>(ShowEntryScreen.class, false, false);;

    @Rule
    public ActivityTestRule<CreateEntryScreen> mActivityInitRule = new ActivityTestRule<>(CreateEntryScreen.class, false, false);;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        TestHelper.initCategories(context);
    }

    @After
    public void tearDown() {
        dbh.close();
    }

    @Test
    public void checkIfAllFieldsAreDisplayed() {
        test_entry = TestHelper.createTestEntryBasic(context);
        Intent intent = new Intent();
        intent.putExtra("entry_id", Integer.toString(test_entry.getId()));
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.out_title)).check(matches(isDisplayed()));
        onView(withId(R.id.out_description)).check(matches(isDisplayed()));
        onView(withId(R.id.out_category)).check(matches(isDisplayed()));
        onView(withId(R.id.out_subcategory)).check(matches(isDisplayed()));
        onView(withId(R.id.out_date)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfAllFieldsDisplayCorrect() {
        test_entry = TestHelper.createTestEntryBasic(context);
        Intent intent = new Intent();
        intent.putExtra("entry_id", Integer.toString(test_entry.getId()));
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.out_title)).check(matches(withText(test_entry.getTitle())));
        onView(withId(R.id.out_description)).check(matches(withText(test_entry.getDescription())));
        onView(withId(R.id.out_date)).check(matches(withText(date_format.format(test_entry.getDate()))));
        onView(withId(R.id.out_category)).check(matches(withText(test_entry.getMainCategory().getName())));
        onView(withId(R.id.out_subcategory)).check(matches(withText(test_entry.getSubCategory().getName())));
    }

    @Test
    public void checkNullProperties() {
        test_entry = TestHelper.createTestEntryBasic(context, "Test", null, 1, 4, null);
        Intent intent = new Intent();
        intent.putExtra("entry_id", Integer.toString(test_entry.getId()));
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.out_title)).check(matches(withText(test_entry.getTitle())));
        onView(withId(R.id.out_description)).check(matches(withText("")));
        onView(withId(R.id.out_date)).check(matches(withText("")));
        onView(withId(R.id.out_category)).check(matches(withText(test_entry.getMainCategory().getName())));
        onView(withId(R.id.out_subcategory)).check(matches(withText(test_entry.getSubCategory().getName())));
    }

    @Test
    public void checkImages() {
        Intent init_intent = new Intent();
        mActivityInitRule.launchActivity(init_intent);
        test_entry = TestHelper.createTestEntryWithPictures(context, mActivityInitRule.getActivity());
        test_entry.addPicture(new Picture("/test/123.jpg", "123.jpg"));
        Intent intent = new Intent();
        intent.putExtra("entry_id", Integer.toString(test_entry.getId()));
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.out_title)).check(matches(withText(test_entry.getTitle())));
        onView(withId(R.id.out_description)).check(matches(withText(test_entry.getDescription())));
        onView(withId(R.id.out_date)).check(matches(withText(date_format.format(test_entry.getDate()))));
        onView(withId(R.id.out_category)).check(matches(withText(test_entry.getMainCategory().getName())));
        onView(withId(R.id.out_subcategory)).check(matches(withText(test_entry.getSubCategory().getName())));
    }

    @Test
    public void checkIfPdfWasCreated() {
        onView(withId(R.id.pdf_export)).perform(click());
        onView(withText("Pdf was created")).
                inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));

    }
}
