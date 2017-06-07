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

import at.sw2017.pocketdiary.database_access.DBEntry;
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
import static org.hamcrest.Matchers.is;

/**
 * Created by marku on 03.06.2017.
 */

@RunWith(AndroidJUnit4.class)
public class StatisticAnalysisInstrumentedTest {

    private DBHandler dbh;

    @Rule
    public ActivityTestRule<StatisticAnalysisActivity> mActivityRule = new ActivityTestRule<>(StatisticAnalysisActivity.class, true, false);

    @Before
    public void setup(){
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        TestHelper.initCategories(context);
        TestHelper.initAddress(context);
        TestHelper.createTestEntryBasic(context);
        TestHelper.initStatistics(context);
        TestHelper.initFriends(context);
        Intents.init();
    }

    @After
    public void tearDown(){
        dbh.close();
        Intents.release();
    }

    @Test
    public void checkIfComponentsAreDisplayed() {

        initializeIntent("2");

        onView(withId(R.id.stat_analysis_btn_calendar)).check(matches(isDisplayed()));
        onView(withId(R.id.stat_analysis_btn_dates)).check(matches(isDisplayed()));
        onView(withId(R.id.stat_analysis_btn_location)).check(matches(isDisplayed()));
        onView(withId(R.id.stat_analysis_btn_friends)).check(matches(isDisplayed()));
        onView(withId(R.id.stat_analysis_btn_camera)).check(matches(isDisplayed()));
        onView(withId(R.id.stat_analysis_categories)).check(matches(isDisplayed()));
        onView(withId(R.id.stat_analysis_delete_stat)).check(matches(isDisplayed()));
        onView(withId(R.id.stat_analysis_lv)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfEventIsDisplayed() {
        initializeIntent();
        onView(allOf(withText("Test"), withParent(withId(R.id.stat_analysis_lv)))).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfEventLeadsToEvent() {
        initializeIntent();
        onView(allOf(withText("Test"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
        intended(hasComponent(ShowEntryScreen.class.getName()));
    }

    @Test
    public void checkIfPoiIsShown() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_location)).perform(click());
        onView(allOf(withText("Poi"), withParent(withId(R.id.stat_analysis_lv)))).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfPoiLeadsToEvent() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_location)).perform(click());
        onView(allOf(withText("Poi"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
        intended(hasComponent(ShowEntryScreen.class.getName()));
    }

    @Test
    public void checkIfStreetIsShown() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_location)).perform(click());
        onView(allOf(withText("Street"), withParent(withId(R.id.stat_analysis_lv)))).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfStreetLeadsToEvent() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_location)).perform(click());
        onView(allOf(withText("Street"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
        intended(hasComponent(ShowEntryScreen.class.getName()));
    }

    @Test
    public void checkIfDateIsShown() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_dates)).perform(click());
        onView(allOf(withText("10.05.2017"), withParent(withId(R.id.stat_analysis_lv)))).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfDateLeadsToEvent() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_dates)).perform(click());
        onView(allOf(withText("10.05.2017"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
        intended(hasComponent(ShowEntryScreen.class.getName()));
    }

    @Test
    public void checkIfCalendarIsCalled() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_calendar)).perform(click());
        intended(hasComponent(ReviewActivity.class.getName()));
    }

    @Test
    public void checkIfCategoryIsDisplayed() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_categories)).perform(click());
        onView(allOf(withText("2 x Sport"), withParent(withId(R.id.stat_analysis_lv)))).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfSubCatsAreDisplayed() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_categories)).perform(click());
        onView(allOf(withText("2 x Sport"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
        onView(withText("Subcategories")).check(matches(isDisplayed()));
        onView(withText("2 x Walking")).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfEventAfterClickOnSubIsDisplayed() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_categories)).perform(click());
        onView(allOf(withText("2 x Sport"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
        onView(withText("Subcategories")).check(matches(isDisplayed()));
        onView(withText("2 x Walking")).perform(click());
        onView(withText("Entries")).check(matches(isDisplayed()));
        onView(withText("Test")).check(matches(isDisplayed()));
        onView(withText("Test_Street")).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfClickEventAfterClickOnSubLeadsToScreen() {
        initializeIntent();
        onView(withId(R.id.stat_analysis_categories)).perform(click());
        onView(allOf(withText("2 x Sport"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
        onView(withText("Subcategories")).check(matches(isDisplayed()));
        onView(withText("2 x Walking")).perform(click());
        onView(withText("Entries")).check(matches(isDisplayed()));
        onView(withText("Test")).perform(click());
        intended(hasComponent(ShowEntryScreen.class.getName()));
    }

    @Test
    public void checkIfFriendsAreDisplayed(){
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_friends)).perform(click());
        onView(allOf(withText("1 x Hermann"), withParent(withId(R.id.stat_analysis_lv)))).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfEventsAreDisplayedAfterClickOnFriend(){
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_friends)).perform(click());
        onView(allOf(withText("1 x Hermann"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
        onView(withText("Test_Street")).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfClickEventAfterClickOnFriendLeadsToScreen(){
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_friends)).perform(click());
        onView(allOf(withText("1 x Hermann"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
        onView(withText("Test_Street")).perform(click());
        intended(hasComponent(ShowEntryScreen.class.getName()));
    }

    @Test
    public void checkIfGridViewIsDisplay(){
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_camera)).perform(click());
        onView(withId(R.id.stat_analysis_delete_stat)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfPictureIsDisplayed(){
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_camera)).perform(click());
        onView(withText("picture")).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfClickOnPictureDisplaysBigPicture(){
        initializeIntent();
        onView(withId(R.id.stat_analysis_btn_camera)).perform(click());
        onView(withText("picture")).perform(click());
        intended(hasComponent(ShowPictureScreen.class.getName()));
    }

    @Test
    public void checkIfEventsAreDisplayedWithOnlyDateFromSet(){
        initializeIntent("3");
        onView(allOf(withText("Test"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
    }

    @Test
    public void checkIfEventsAreDisplayedWithOnlyDateUntilSet(){
        initializeIntent("4");
        onView(allOf(withText("Test"), withParent(withId(R.id.stat_analysis_lv)))).perform(click());
    }

    @Test
    public void checkIfClickOnDeleteShowsAlert(){
        initializeIntent();
        onView(withId(R.id.stat_analysis_delete_stat)).perform(click());
        onView(withText("Do you want to delete this statistic?")).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfClickOnYesInAlertLeadsToNewActivity(){
        initializeIntent();
        onView(withId(R.id.stat_analysis_delete_stat)).perform(click());
        onView(withText("Yes")).perform(click());
        intended(hasComponent(StatisticScreenActivity.class.getName()));
    }

    private void initializeIntent(String statistic_id){
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, StatisticAnalysisActivity.class);
        intent.putExtra("statistic_id", statistic_id);
        mActivityRule.launchActivity(intent);
    }

    private void initializeIntent(){
        initializeIntent("2");
    }

}
