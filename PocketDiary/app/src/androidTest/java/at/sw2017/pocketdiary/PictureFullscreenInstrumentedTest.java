package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class PictureFullscreenInstrumentedTest {
    private DBHandler dbh;
    private String[] file_paths;
    private Entry test_entry;

    @Rule
    public ActivityTestRule<PictureFullscreen> mActivityRule = new ActivityTestRule<>(PictureFullscreen.class, false, false);;

    @Rule
    public ActivityTestRule<CategoriesActivity> mActivityInitRule = new ActivityTestRule<>(CategoriesActivity.class, false, false);;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        Intent init_intent = new Intent();
        Intents.init();
        mActivityInitRule.launchActivity(init_intent);
        TestHelper.grantPicturePermissions();
        Entry test_entry = TestHelper.createTestEntryWithPictures(context, mActivityInitRule.getActivity());
        Intent intent = new Intent();
        intent.putExtra("picture_id", Integer.toString(test_entry.getPictures().get(0).getId()));
        mActivityRule.launchActivity(intent);
    }

    @After
    public void tearDown() {
        Intents.release();
        dbh.close();
    }

    @Test
    public void checkCreateFullscreenImage() {
        onView(withId(R.id.image_fullscreen)).check(matches(isDisplayed()));
    }
}
