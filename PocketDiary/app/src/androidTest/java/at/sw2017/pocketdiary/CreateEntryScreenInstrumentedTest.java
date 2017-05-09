package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.DecimalFormat;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class CreateEntryScreenInstrumentedTest {

    private DBHandler dbh;
    private DBEntry dbe;
    private DBAddress dba;
    private Geocoder geocoder;

    private String titleToBeTyped;

    @Rule
    public ActivityTestRule<CreateEntryScreen> mActivityRule = new ActivityTestRule<>(CreateEntryScreen.class, false, false);

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        Helper.initCategories(context);
        dbe = new DBEntry(context);
        dba = new DBAddress(context);
        geocoder = new Geocoder(context);
        titleToBeTyped = "Running";
        Intents.init();
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void release() {
        dbh.close();
        dbe.close();
        dba.close();
        Intents.release();
    }

    @Test
    public void checkIfAllFieldsAreDisplayed() {
        onView(withId(R.id.out_title)).check(matches(isDisplayed()));
        onView(withId(R.id.input_description)).check(matches(isDisplayed()));
        onView(withId(R.id.out_category)).check(matches(isDisplayed()));
        onView(withId(R.id.out_description)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_calendar)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_friends)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_location)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_pictures)).check(matches(isDisplayed()));
    }

    @Test
    public void createInvalidEntryTitleOnly() {
        onView(withId(R.id.out_title)).perform(typeText(titleToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void createInvalidEntryCategoriesOnly() {
        onView(withId(R.id.out_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.out_category)).check(matches(not(withText("Sport"))));
        onView(withId(R.id.out_description)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.out_description)).check(matches(not(withText("Running"))));
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void createInvalidEntryNoDate() {
        onView(withId(R.id.out_title)).perform(typeText(titleToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.out_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.out_category)).check(matches(not(withText("Sport"))));
        onView(withId(R.id.out_description)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.out_description)).check(matches(not(withText("Running"))));
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void createValidEntry() {
        onView(withId(R.id.out_title)).perform(typeText(titleToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.out_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.out_category)).check(matches(not(withText("Sport"))));
        onView(withId(R.id.out_description)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.out_description)).check(matches(not(withText("Running"))));
        onView(withId(R.id.btn_calendar)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 4, 3));
        onView(withId(android.R.id.button1)).perform(click()); //click on dialog positive button
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }

    @Test
    public void pressCancelButton() {
        onView(withId(R.id.btn_cancel)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }

    @Test
    public void pressLocationButton() {
        Address address_test = new Address(23.4500, 45.4500);
        onView(withId(R.id.out_title)).perform(typeText(titleToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.out_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.out_category)).check(matches(not(withText("Sport"))));
        onView(withId(R.id.input_subcategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.input_subcategory)).check(matches(not(withText("Running"))));
        onView(withId(R.id.btn_calendar)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 4, 3));
        onView(withId(android.R.id.button1)).perform(click()); //click on dialog positive button
        onView(withId(R.id.btn_location)).perform(click());
        onView(withId(R.id.badge_address)).check(matches((isDisplayed())));
        onView(withId(R.id.btn_save)).perform(click());
        Entry entry;
        Context context = InstrumentationRegistry.getTargetContext();
        entry = Helper.getEntryComplete(context, 1);
        DecimalFormat df2 = new DecimalFormat("###.##");
        double latitude = Double.valueOf(df2.format(entry.getAddress().getLatitude()));
        assertTrue(address_test.getLatitude() == latitude);
    }

    @Test
    public void reverseGeocoding() throws IOException {
        Address address = new Address(13.0, 43.0);
        ReverseGeocoder reverseGeocoder = new ReverseGeocoder();
        Address address1;
        address1 = reverseGeocoder.getAddress(address.getLongitude(), address.getLatitude(), geocoder);
        assertTrue(address1.getCountry().equals("Italy"));
    }
}