package at.sw2017.pocketdiary;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.not;

public class StartScreenInstrumentedTest {

    private String camera_package = "com.android.camera";
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private DBHandler dbh;
    private DBUserSetting dbs;

    @Rule
    public ActivityTestRule<StartScreen> mActivityRule = new ActivityTestRule<StartScreen>(StartScreen.class, false, false);

    @Before
    public void setUp() {
        Intents.init();
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbs = new DBUserSetting(context);
        UserSetting setting = new UserSetting();
        setting.setUserName("TestUser");
        setting.setFilePath("");
        setting.setPin("123");
        setting.setPinActive("0");
        dbs.insert(setting);
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void release() {
        Intents.release();
        dbh.close();
        dbs.close();
    }

    @Test
    public void testButtons() {
        onView(withId(R.id.create_entry)).check(matches(isClickable()));
        onView(withId(R.id.review)).check(matches(isClickable()));
        onView(withId(R.id.statistic)).check(matches(isClickable()));
        onView(withId(R.id.settings)).check(matches(isClickable()));
        //onView(withId(R.id.pictureField)).check(matches(isClickable()));
    }

    @Test
    public void pressCreateEntryButton() {
        onView(withId(R.id.create_entry)).perform(click());
        intended(hasComponent(CreateEntryScreen.class.getName()));
    }

    @Test
    public void pressCreateReviewButton() {
        onView(withId(R.id.review)).perform(click());
        intended(hasComponent(ReviewActivity.class.getName()));
    }

    @Test
    public void pressSettingButton() {
        onView(withId(R.id.settings)).perform(click());
        intended(hasComponent(SettingScreen.class.getName()));
    }

    @Test
    public void pressProfilPictureCamera() {
        TestHelper.grantPicturePermissions();
        onView(withId(R.id.pictureField)).perform(click());
        onView(withText("Camera")).check(matches(isDisplayed()));

        Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.mipmap.ic_launcher_round);

        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(CAMERA_REQUEST, resultData);

        intending(toPackage(camera_package)).respondWith(result);
        onView(withText("Camera")).perform(click());
        intended(toPackage(camera_package));
        List<UserSetting> temp = dbs.getUserSetting(1);
        UserSetting user_setting = temp.get(0);
        assertTrue(!user_setting.getFilePath().equals(""));
    }

    @Test
    public void checkGalleryNoSelect() {
        TestHelper.grantPicturePermissions();
        Intent resultData = new Intent();
        resultData.setData(null);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PICK_IMAGE_REQUEST, resultData);
        intending(not(isInternal())).respondWith(result);
        onView(withId(R.id.pictureField)).perform(click());
        onView(withText("Gallery")).check(matches(isDisplayed()));
        onView(withText("Gallery")).perform(click());
        List<UserSetting> temp = dbs.getUserSetting(1);
        UserSetting user_setting = temp.get(0);
        assertTrue(user_setting.getFilePath().equals(""));
    }

    @Test
    public void pressProfilPictureGallery() {
        TestHelper.grantPicturePermissions();
        Uri uri = TestHelper.insertTestImageToCameraGetPathGetUri(mActivityRule.getActivity());
        Intent resultData = new Intent();
        resultData.setData(uri);
        Cursor cursor = mActivityRule.getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String file_path = cursor.getString(idx);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PICK_IMAGE_REQUEST, resultData);
        intending(not(isInternal())).respondWith(result);
        TestHelper.grantPicturePermissions();
        onView(withId(R.id.pictureField)).perform(click());
        onView(withText("Gallery")).check(matches(isDisplayed()));
        onView(withText("Gallery")).perform(click());
        List<UserSetting> temp = dbs.getUserSetting(1);
        UserSetting user_setting = temp.get(0);
        assertTrue(user_setting.getFilePath().equals(file_path));
    }
}