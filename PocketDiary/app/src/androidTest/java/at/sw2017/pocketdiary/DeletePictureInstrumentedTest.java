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

import java.util.Arrays;

import at.sw2017.pocketdiary.database_access.DBHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class DeletePictureInstrumentedTest {

    private DBHandler dbh;
    private String[] file_paths;

    @Rule
    public ActivityTestRule<DeletePictureScreen> mActivityRule = new ActivityTestRule<>(DeletePictureScreen.class, false, false);;

    @Rule
    public ActivityTestRule<CreateEntryScreen> mActivityInitRule = new ActivityTestRule<>(CreateEntryScreen.class, false, false);;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        Intent init_intent = new Intent();
        Intents.init();
        mActivityInitRule.launchActivity(init_intent);
        TestHelper.grantPicturePermissions();
        String file_path_one = TestHelper.insertTestImageToCameraGetPath(mActivityInitRule.getActivity());
        String file_path_two = TestHelper.insertTestImageToCameraGetPath(mActivityInitRule.getActivity());
        String file_path_invalid = "/test/123.jpg";
        Intent intent = new Intent();
        file_paths = new String[] { file_path_one, file_path_two, file_path_invalid };
        intent.putExtra("file_paths", file_paths);
        mActivityRule.launchActivity(intent);
    }

    @After
    public void tearDown() {
        Intents.release();
        dbh.close();
    }

    @Test
    public void checkIfEverythingIsDisplayed() {
        onView(withId(R.id.gridview)).check(matches(isDisplayed()));
        onView(withId(0)).check(matches(isDisplayed()));
        onView(withId(1)).check(matches(isDisplayed()));
    }

    @Test
    public void showAlert() {
        onView(withId(R.id.gridview)).check(matches(isDisplayed()));
        onView(withId(0)).perform(click());
        onView(withText("Do you really want to remove the picture?")).check(matches(isDisplayed()));
    }

    @Test
    public void acceptAlert() {
        onView(withId(R.id.gridview)).check(matches(isDisplayed()));
        onView(withId(0)).perform(click());
        onView(withText("Do you really want to remove the picture?")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        onView(withId(0)).check(matches(not(isDisplayed())));
    }

    @Test
    public void cancelAlert() {
        onView(withId(R.id.gridview)).check(matches(isDisplayed()));
        onView(withId(0)).perform(click());
        onView(withText("Do you really want to remove the picture?")).check(matches(isDisplayed()));
        onView(withText("Cancel")).perform(click());
        onView(withId(0)).check(matches(isDisplayed()));
    }

    @Test
    public void checkBackButton() {
        onView(withId(R.id.gridview)).check(matches(isDisplayed()));
        onView(withId(0)).perform(click());
        onView(withText("Do you really want to remove the picture?")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        onView(withId(0)).check(matches(not(isDisplayed())));
        mActivityRule.getActivity().onBackPressed();
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void checkPathUpdate() {
        onView(withId(R.id.gridview)).check(matches(isDisplayed()));
        assertTrue(file_paths.length == mActivityRule.getActivity().file_paths.length);
        onView(withId(0)).perform(click());
        onView(withText("Do you really want to remove the picture?")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        onView(withId(0)).check(matches(not(isDisplayed())));
        String[] file_paths_return = Arrays.copyOfRange(file_paths, 1, file_paths.length);
        mActivityRule.getActivity().updateFilePaths();
        assertTrue(file_paths_return.length == mActivityRule.getActivity().file_paths.length);
    }

    @Test
    public void checkGetItem() {
        onView(withId(R.id.gridview)).check(matches(isDisplayed()));
        assertTrue(file_paths.length == mActivityRule.getActivity().file_paths.length);
        onView(withId(0)).perform(click());
        onView(withText("Do you really want to remove the picture?")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        onView(withId(0)).check(matches(not(isDisplayed())));
        String[] file_paths_return = Arrays.copyOfRange(file_paths, 1, file_paths.length);
        mActivityRule.getActivity().updateFilePaths();
        assertTrue(file_paths_return.length == mActivityRule.getActivity().file_paths.length);
    }
}
