package at.sw2017.pocketdiary;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Camera;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

public class StartScreenInstrumentedTest {
    @Rule
    public ActivityTestRule<StartScreen> mActivityRule = new ActivityTestRule<StartScreen>(StartScreen.class);

    @Before
    public void setUp() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void release() {
        Intents.release();
    }


    @Test
    public void testButtons() throws Exception {
        onView(withId(R.id.create_entry)).check(matches(isClickable()));
        onView(withId(R.id.review)).check(matches(isClickable()));
        onView(withId(R.id.statistic)).check(matches(isClickable()));
        onView(withId(R.id.settings)).check(matches(isClickable()));
        onView(withId(R.id.pictureField)).check(matches(isClickable()));
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
        onView(withId(R.id.pictureField)).perform(click());
        onView(withText("Camera")).check(matches(isDisplayed()));

        onView(withText("Camera")).perform(click());
        intended(toPackage("com.android.camera"));

    }
    @Test
    public void pressProfilPictureGallery() {
        onView(withId(R.id.pictureField)).perform(click());
        onView(withText("Gallery")).check(matches(isDisplayed()));

        onView(withText("Gallery")).perform(click());
        intended(toPackage("com.android.gallery"));
    }
}
