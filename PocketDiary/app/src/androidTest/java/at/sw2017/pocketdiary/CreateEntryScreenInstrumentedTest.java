package at.sw2017.pocketdiary;

import android.app.Instrumentation;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBFriend;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBPicture;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
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
    private DBPicture dbp;
    private DBUserSetting dbs;
    private Geocoder geocoder;
    private Context context;
    private String camera_package = "com.android.camera";
    private UiDevice mDevice;

    private static final int CAMERA_REQUEST = 1;
    private static final int WRITE_STORAGE_REQUEST = 2;
    private static final int FINE_LOCATION_REQUEST = 3;
    private static final int COARSE_LOCATION_REQUEST = 4;
    private static final int PICK_IMAGE_REQUEST = 5;
    private static final int DELETE_IMAGE_REQUEST = 6;
    private static final int REQUEST_NOT_OK = -1;

    private String titleToBeTyped;

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
        dbs = new DBUserSetting(context);
        TestHelper.createUserSetting(context);
        geocoder = new Geocoder(context);
        titleToBeTyped = "Running";
        Intents.init();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void release() {
        dbh.close();
        dbe.close();
        dba.close();
        dbp.close();
        Intents.release();
    }

    @Test
    public void checkIfAllFieldsAreDisplayed() {
        onView(withId(R.id.input_title)).check(matches(isDisplayed()));
        onView(withId(R.id.input_description)).check(matches(isDisplayed()));
        onView(withId(R.id.input_category)).check(matches(isDisplayed()));
        onView(withId(R.id.input_description)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_calendar)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_friends)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_location)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_pictures)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_save)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_cancel)).check(matches(isDisplayed()));
    }

    @Test
    public void createInvalidEntryTitleOnly() {
        onView(withId(R.id.input_title)).perform(typeText(titleToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void createInvalidEntryCategoriesOnly() {
        onView(withId(R.id.input_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.input_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.input_category)).check(matches(not(withText("Sport"))));
        onView(withId(R.id.input_subcategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.input_subcategory)).check(matches(not(withText("Running"))));
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void createInvalidEntryMainCategoryOnly() {
        onView(withId(R.id.input_title)).perform(typeText(titleToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.input_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.input_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.input_category)).check(matches(not(withText("Sport"))));
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void testIfNothingSelectedForDropdowns() {
        onView(withId(R.id.input_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.input_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        onView(withId(R.id.input_category)).check(matches(not(withText(""))));
        onView(withId(R.id.input_subcategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        onView(withId(R.id.input_subcategory)).check(matches(not(withText(""))));
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void createInvalidEntryNoDate() {
        onView(withId(R.id.input_title)).perform(typeText(titleToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.input_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.input_category)).check(matches(not(withText("Sport"))));
        onView(withId(R.id.input_subcategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.input_subcategory)).check(matches(not(withText("Running"))));
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void createValidEntry() {
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
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }

    @Test
    public void checkCamera() {
        TestHelper.grantPicturePermissions();
        String test_image_path = TestHelper.insertTestImageToCameraGetPath(mActivityRule.getActivity());
        Bitmap icon = BitmapFactory.decodeFile(test_image_path);
        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(CAMERA_REQUEST, resultData);
        intending(toPackage(camera_package)).respondWith(result);
        onView(withId(R.id.btn_pictures)).perform(click());
        onView(withText("Camera")).perform(click());
        onView(withId(R.id.badge_camera)).check(matches(withText("1")));
    }

    @Test
    public void checkGalleryNoSelect() {
        TestHelper.grantPicturePermissions();
        Intent resultData = new Intent();
        resultData.setData(null);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PICK_IMAGE_REQUEST, resultData);
        intending(not(isInternal())).respondWith(result);
        onView(withId(R.id.btn_pictures)).perform(click());
        onView(withText("Gallery")).perform(click());
        onView(withId(R.id.badge_camera)).check(matches(withText("0")));
    }

    @Test
    public void checkGallerySingleSelect() {
        TestHelper.grantPicturePermissions();
        Uri uri = TestHelper.insertTestImageToCameraGetPathGetUri(mActivityRule.getActivity());
        Intent resultData = new Intent();
        resultData.setData(uri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PICK_IMAGE_REQUEST, resultData);
        intending(not(isInternal())).respondWith(result);
        onView(withId(R.id.btn_pictures)).perform(click());
        onView(withText("Gallery")).perform(click());
        onView(withId(R.id.badge_camera)).check(matches(withText("1")));
    }

    @Test
    public void checkGalleryMultiSelect() {
        Uri uri = TestHelper.insertTestImageToCameraGetPathGetUri(mActivityRule.getActivity());
        Intent resultData = new Intent();
        ClipData.Item item_one = new ClipData.Item(uri);
        ClipData.Item item_two = new ClipData.Item(uri);
        ClipData.Item item_three = new ClipData.Item(uri);
        String[] mime_types = new String[] {"image/jpeg", "image/bmp", "image/gif", "image/jpg", "image/png"};
        ClipData clip_data = new ClipData("test", mime_types, item_one);
        clip_data.addItem(item_two);
        clip_data.addItem(item_three);
        resultData.setClipData(clip_data);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PICK_IMAGE_REQUEST, resultData);
        intending(not(isInternal())).respondWith(result);
        onView(withId(R.id.btn_pictures)).perform(click());
        onView(withText("Gallery")).perform(click());
        onView(withId(R.id.badge_camera)).check(matches(withText("3")));
    }

    @Test
    public void checkPictureDelete() {
        TestHelper.grantPicturePermissions();
        String test_image_path = TestHelper.insertTestImageToCameraGetPath(mActivityRule.getActivity());
        Intent resultData = new Intent();
        String[] file_paths = new String[] { test_image_path };
        resultData.putExtra("file_paths", file_paths);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(DELETE_IMAGE_REQUEST, resultData);
        intending(toPackage(mActivityRule.getActivity().getPackageName())).respondWith(result);
        onView(withId(R.id.btn_pictures)).perform(click());
        onView(withText("Remove")).perform(click());
        onView(withId(R.id.badge_camera)).check(matches(withText("1")));
    }

    @Test
    public void checkRequestAborted() {
        TestHelper.grantPicturePermissions();
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(REQUEST_NOT_OK, resultData);
        intending(toPackage(camera_package)).respondWith(result);
        onView(withId(R.id.btn_pictures)).perform(click());
        onView(withText("Camera")).perform(click());
        onView(withId(R.id.badge_camera)).check(matches(withText("0")));
    }

    @Test
    public void pressCancelButton() {
        onView(withId(R.id.btn_cancel)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }

    @Test
    public void createEntryWithAddressStreet() {
        Address address_test = new Address(23, 45);
        address_test.setStreet("Inffeldgasse 10");
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
        mActivityRule.getActivity().entry_address = address_test;
        onView(withId(R.id.btn_save)).perform(click());
        Entry entry;
        Context context = getTargetContext();
        entry = Helper.getEntryComplete(context, 1);
        DecimalFormat df2 = new DecimalFormat("###.##");
        double latitude = Double.valueOf(df2.format(entry.getAddress().getLatitude()));
        assertTrue(address_test.getLatitude() == latitude);
    }

    @Test
    public void createEntryWithPictures() {
        TestHelper.grantPicturePermissions();
        String path_one = "/test/1.jpg";
        String path_two = "/test/2.jpg";
        String path_three = "/test/3.jpg";
        List<String> picture_paths = new ArrayList<>();
        picture_paths.add(path_one);
        picture_paths.add(path_two);
        picture_paths.add(path_three);
        Picture picture = new Picture(path_one, "1.jpg");
        long id = dbp.insert(picture);
        assertTrue((int) id > 0);
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
        mActivityRule.getActivity().entry_picture_paths = picture_paths;
        onView(withId(R.id.btn_save)).perform(click());
        Entry entry;
        Context context = getTargetContext();
        entry = Helper.getEntryComplete(context, 1);
        assertTrue(picture_paths.size() == entry.getPictures().size());
    }

    @Test
    public void createEntryWithAddressLatitudeLongitude() {
        TestHelper.grantLocationPermissions();
        Address address_test = new Address(23, 45);
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
        mActivityRule.getActivity().entry_address = address_test;
        onView(withId(R.id.btn_save)).perform(click());
        Entry entry;
        Context context = getTargetContext();
        entry = Helper.getEntryComplete(context, 1);
        DecimalFormat df2 = new DecimalFormat("###.##");
        double latitude = Double.valueOf(df2.format(entry.getAddress().getLatitude()));
        assertTrue(address_test.getLatitude() == latitude);
    }

    @Test
    public void pressLocationButton() {
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
        onView(withId(R.id.badge_address)).check(matches(isDisplayed()));
    }

/*
    @Test
    public void shouldOpenAlertDialog(){
        Address address_test = new Address(23.4500, 45.4500);
        onView(withId(R.id.btn_location)).perform(click());
        onView(withText("GPS Not Enabled")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldCancelAlertDialog(){
        Address address_test = new Address(23.4500, 45.4500);
        onView(withId(R.id.btn_location)).perform(click());
        onView(withText("GPS Not Enabled")).check(matches(isDisplayed()));
        onView(withText("No")).perform(click());
        onView(withId(R.id.btn_location)).perform(click());
        onView(withText("GPS Not Enabled")).check(matches(isDisplayed()));
    }*/

    @Test
    public void checkGetLocation() {
        TestHelper.grantLocationPermissions();
        GpsLocation gps_location = new GpsLocation(context);
        //enable GPS to test this
        Location current_location = new Location("A1");
        current_location.setLongitude(15);
        current_location.setLatitude(13);
        gps_location.current_location = current_location;
        assertTrue(gps_location.getLocation() != null);
        //disable GPS to test this
        /*
        gps_location.current_location = null;
        assertTrue(gps_location.getLocation() == null);
        */
    }

    /*@Test
    public void checkGetLocationNoPermission() throws UiObjectNotFoundException {
        TestHelper.revokePicturePermissions();
        onView(withId(R.id.btn_location)).perform(click());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UiObject allowPermissions = mDevice.findObject(new UiSelector().text("Verweigern"));
            if (allowPermissions.exists()) {
                allowPermissions.click();
            }
        }
        GpsLocation gps_location = new GpsLocation(context);
        Location current_location = new Location("A1");
        current_location.setLongitude(15);
        current_location.setLatitude(13);
        gps_location.current_location = current_location;
        Location location = gps_location.getLocation();
        assertTrue(location == null);
    }*/

    @Test
    public void checkGetLocationWithPermission() {
        TestHelper.grantLocationPermissions();
        GpsLocation gps_location = new GpsLocation(context);
        Location current_location = new Location("A1");
        current_location.setLongitude(15);
        current_location.setLatitude(13);
        gps_location.current_location = current_location;

        Location location = gps_location.getLocation();
        assertTrue(location.getLatitude() != 0);
    }


    /*@Test
    public void shouldAcceptAlertDialog() throws Throwable {
        Address address_test = new Address(23.4500, 45.4500);
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
    }*/

    /*@Test
    public void reverseGeocoding() throws IOException {
        Address address = new Address(13.0, 43.0);
        ReverseGeocoder reverseGeocoder = new ReverseGeocoder();
        Address address1;
        address1 = reverseGeocoder.getAddress(address.getLongitude(), address.getLatitude(), geocoder);
        assertTrue(address1.getCountry().equals("Italy"));
    }*/

    @Test
    public void addFriends() {
        Friend friend = new Friend("Stefan", false);
        Friend friend2 = new Friend("Stefan2", false);
        DBFriend dbf = new DBFriend(mActivityRule.getActivity());
        dbf.insert(friend);
        dbf.insert(friend2);

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
        onView(withId(R.id.multi_spinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.btn_friends)).perform(click());
        onView(withId(R.id.multi_spinner)).check(matches(isDisplayed()));
        onView(withId(R.id.multi_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btn_save)).perform(click());

        Entry entry;
        Context context = InstrumentationRegistry.getTargetContext();
        entry = Helper.getEntryComplete(context, 1);
        String friend_ = entry.getAllFriends();
        Friend temp_friend;
        DBFriend dbf2 = new DBFriend(mActivityRule.getActivity());
        temp_friend = dbf2.getFriend(Integer.parseInt(friend_));
        assertTrue("Stefan2".equals(temp_friend.getName()));
    }
}