package at.sw2017.pocketdiary;

import android.app.Instrumentation;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
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
    public void checkIfAllFieldsAreDisplayed() {
        initActivity("0");
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
        initActivity("0");
        onView(withId(R.id.input_title)).perform(typeText(titleToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void createInvalidEntryCategoriesOnly() {
        initActivity("0");
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
        initActivity("0");
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
        initActivity("0");
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
        initActivity("0");
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
        initActivity("0");
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

    /*@Test
    public void checkCamera() {
        initActivity("0");
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
    }*/

    @Test
    public void checkGalleryNoSelect() {
        initActivity("0");
        TestHelper.grantPicturePermissions();
        Intent resultData = new Intent();
        resultData.setData(null);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PICK_IMAGE_REQUEST, resultData);
        intending(not(isInternal())).respondWith(result);
        onView(withId(R.id.btn_pictures)).perform(click());
        onView(withText("Gallery")).perform(click());
        onView(withId(R.id.badge_camera)).check(matches(withText("0")));
    }

    /*@Test
    public void checkGallerySingleSelect() {
        initActivity("0");
        TestHelper.grantPicturePermissions();
        Uri uri = TestHelper.insertTestImageToCameraGetPathGetUri(mActivityRule.getActivity());
        Intent resultData = new Intent();
        resultData.setData(uri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PICK_IMAGE_REQUEST, resultData);
        intending(not(isInternal())).respondWith(result);
        onView(withId(R.id.btn_pictures)).perform(click());
        onView(withText("Gallery")).perform(click());
        onView(withId(R.id.badge_camera)).check(matches(withText("1")));
    }*/

    /*@Test
    public void checkGalleryMultiSelect() {
        initActivity("0");
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
    }*/

    @Test
    public void checkPictureDelete() {
        initActivity("0");
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
        initActivity("0");
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
        initActivity("0");
        onView(withId(R.id.btn_cancel)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }

    @Test
    public void createEntryWithPictures() {
        initActivity("0");
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
    public void createEntryWithAddressStreet() {
        initActivity("0");
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
    public void updateEntryWithoutAddress() {
        Entry test_entry = TestHelper.createTestEntryBasic(context);
        String entry_id = String.valueOf(test_entry.getId());
        initActivity(entry_id);
        onView(withId(R.id.input_subcategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.input_subcategory)).check(matches(not(withText("Running"))));
        onView(withId(R.id.input_title)).check(matches(withText(test_entry.getTitle())));
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }

    @Test
    public void updateEntryWithAddress() {
        Entry test_entry = TestHelper.createTestEntryBasic(context);
        Address address = new Address("Teststreet", 12, 15);
        int address_id = (int) dba.insert(address);
        test_entry.setAddress(address);
        test_entry.setAddressId(address_id);
        dbe.updateEntry(test_entry);
        String entry_id = String.valueOf(test_entry.getId());
        initActivity(entry_id);
        onView(withId(R.id.input_subcategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.input_subcategory)).check(matches(not(withText("Running"))));
        onView(withId(R.id.input_title)).check(matches(withText(test_entry.getTitle())));
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }
    @Test
    public void updateEntryWithNewAddress() {
        Entry test_entry = TestHelper.createTestEntryBasic(context);
        Address edit_address = new Address("Teststreet_Edit", 13, 16);
        dbe.updateEntry(test_entry);
        String entry_id = String.valueOf(test_entry.getId());
        initActivity(entry_id);
        onView(withId(R.id.input_subcategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        onView(withId(R.id.input_subcategory)).check(matches(not(withText("Running"))));
        onView(withId(R.id.input_title)).check(matches(withText(test_entry.getTitle())));
        mActivityRule.getActivity().entry_address = edit_address;
        onView(withId(R.id.btn_save)).perform(click());
        intended(hasComponent(StartScreen.class.getName()));
    }

    @Test
    public void createEntryWithAddressLatitudeLongitude() {
        initActivity("0");
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
    public void addFriends() {
        initActivity("0");
        Friend friend = new Friend("Stefan", false);
        Friend friend2 = new Friend("Stefan2", false);
        DBFriend dbf = new DBFriend(mActivityRule.getActivity());
        dbf.insert(friend);
        dbf.insert(friend2);

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