package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBPicture;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by Sandra on 07.06.2017.
 */

public class CreateEntryLocationInstrumentedTest {

    private DBHandler dbh;
    private DBEntry dbe;
    private DBAddress dba;
    private DBPicture dbp;
    private Geocoder geocoder;
    private Context context;

    private String titleToBeTyped = "Running";

    @Rule
    public ActivityTestRule<CreateEntryScreen> mActivityRule = new ActivityTestRule<>(CreateEntryScreen.class, false, false);

    @Before
    public void setUp() {
        context = getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        Helper.initCategories(context);
        dbe = new DBEntry(context);
        dba = new DBAddress(context);
        dbp = new DBPicture(context);
        geocoder = new Geocoder(context);
        Intents.init();
    }

    @After
    public void release() {
        dbh.close();
        dbe.close();
        dba.close();
        dbp.close();
        Intents.release();
    }

    public void initActivity(String entry_id) {
        Intent intent = new Intent();
        intent.putExtra("entry_id", entry_id);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void pressLocationButton() {
        initActivity("0");
        TestHelper.grantLocationPermissions();
        onView(withId(R.id.input_title)).perform(typeText(titleToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.input_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.input_category)).check(matches(not(withText("Sport"))));
        onView(withId(R.id.input_subcategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.input_subcategory)).check(matches(not(withText("Running"))));
        onView(withId(R.id.btn_calendar)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 4, 3));
        onView(withId(android.R.id.button1)).perform(click()); //click on dialog positive button
        onView(withId(R.id.btn_location)).perform(click());
    }

    @Test
    public void checkGetLocationWithPermission() {
        initActivity("0");
        TestHelper.grantLocationPermissions();
        GpsLocation gps_location = new GpsLocation(context);
        Location current_location = new Location("A1");
        current_location.setLongitude(15);
        current_location.setLatitude(13);
        gps_location.current_location = current_location;

        Location location = gps_location.getLocation();
        assertTrue(location.getLatitude() != 0);
    }

    /* ======================== Files with disabled GPS on Phone ============================== */
    /*@Test
    public void shouldOpenAlertDialog(){
        initActivity("0");
        onView(withId(R.id.btn_location)).perform(click());
        onView(withText("GPS Not Enabled")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldCancelAlertDialog(){
        initActivity("0");
        onView(withId(R.id.btn_location)).perform(click());
        onView(withText("GPS Not Enabled")).check(matches(isDisplayed()));
        onView(withText("No")).perform(click());
        onView(withId(R.id.btn_location)).perform(click());
        onView(withText("GPS Not Enabled")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldAcceptAlertDialog() throws Throwable {
        initActivity("0");
        onView(withId(R.id.btn_location)).perform(click());
        onView(withText("GPS Not Enabled")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivityRule.getActivity().onBackPressed();
            }
        });
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void checkGetLocation() {
        initActivity("0");
        TestHelper.grantLocationPermissions();
        GpsLocation gps_location = new GpsLocation(context);
        //enable GPS to test this
        Location current_location = new Location("A1");
        current_location.setLongitude(15);
        current_location.setLatitude(13);
        gps_location.current_location = current_location;
        assertTrue(gps_location.getLocation() != null);
        //disable GPS to test this
        //gps_location.current_location = null;
        //assertTrue(gps_location.getLocation() == null);
    }*/

    /* Set Lat/Long of Emulator to 13.0/43.0*/
    /*@Test
    public void reverseGeocoding() throws IOException {
        initActivity("0");
        Address address = new Address(13.0, 43.0);
        ReverseGeocoder reverseGeocoder = new ReverseGeocoder();
        Address address1;
        address1 = reverseGeocoder.getAddress(address.getLongitude(), address.getLatitude(), geocoder);
        SystemClock.sleep(2000);
        assertTrue(address1.getCountry().equals("Italy"));
    }*/
}
