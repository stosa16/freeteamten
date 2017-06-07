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

import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBStatistic;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by marku on 26.05.2017.
 */

@RunWith(AndroidJUnit4.class)
public class StatisticScreenInstrumentedTest {

    private DBHandler dbh;
    private DBStatistic dbs;

    @Rule
    public ActivityTestRule<StatisticScreenActivity> mActivityRule = new ActivityTestRule<>(StatisticScreenActivity.class, false, false);

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbs = new DBStatistic(context);
        TestHelper.initCategories(context);
        TestHelper.createTestEntryBasic(context);
        TestHelper.initStatistics(context);
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
        onView(withId(R.id.statistic_create_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.statistic_saved_txt)).check(matches(isDisplayed()));
        onView(withId(R.id.statistic_listview)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldDisplayStatistic(){
        onView(allOf(withText("Statistic"), withParent(withId(R.id.statistic_listview)))).check(matches(isDisplayed()));
    }

    @Test
    public void clickCreateBtn(){
        onView(withId(R.id.statistic_create_btn)).perform(click());
        intended(hasComponent(CreateStatisticActivity.class.getName()));
    }
}
