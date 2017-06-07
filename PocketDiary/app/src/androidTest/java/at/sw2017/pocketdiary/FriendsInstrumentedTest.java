package at.sw2017.pocketdiary;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBFriend;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;
/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith ( AndroidJUnit4 . class)
public class FriendsInstrumentedTest {
    private DBHandler dbh;
    private DBEntry dbe;
    private DBFriend dbf;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbe = new DBEntry(context);
        dbf = new DBFriend(context);
    }

    @After
    public void tearDown() throws Exception {
        dbh.close();
        dbe.close();
        dbf.close();
    }

    @Rule
    public ActivityTestRule<Friends> mActivityRule = new ActivityTestRule<>(
            Friends.class);
    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.


        /*onView(withId(R.id.button))
                .perform(click());*/


        /*onView(withId(R.id.changeTextBt)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.textToBeChanged))
                .check(matches(withText(mStringToBetyped)));*/
    }

    @Test
    public void testButtons () {
        onView( withText( "Add" )). perform ( click());
    }

    @Test
    public void checkIfInputTextIsEmpty() {
        onView(withId(R.id.editText)).check(ViewAssertions.matches(withHint("Name")));
    }

    @Test
    public void shouldInsertName() {
        String test_text = "Some Name";
        onView(withId(R.id.editText)).perform(clearText(), typeText(test_text));
        Espresso.closeSoftKeyboard();
        onView(allOf(withId(R.id.editText), withText(test_text))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldAddFriend() throws Exception {
        Friend friend = new Friend("Stefan", false);
        long id = dbf.insert(friend);
        assertTrue(id > 0);
        Friend loaded_friend = dbf.getFriend((int)id);
        assertTrue(loaded_friend.getId() == (int)id);
        assertTrue(loaded_friend.getName().equals(friend.getName()));
        assertTrue(loaded_friend.isDeleted() == friend.isDeleted());
        //return friend;
    }

    @Test
    public void shouldGetAllFriends(){
        Friend friend = new Friend("Stefan", false);
        dbf.insert(friend);
        List<Friend> friends_loaded = dbf.getAllFriends();
        Friend friend_loaded = friends_loaded.get(0);
        assertTrue(friend.getName().equals(friend_loaded.getName()));
    }

    @Test
    public void shouldDeleteFriend() {
        Friend friend = new Friend("Stefan", false);
        dbf.insert(friend);
        List<Friend> friends_loaded = dbf.getAllFriends();
        friends_loaded.get(0).setDeleted(true);
        dbf.updateFriend(friends_loaded.get(0));
        List<Friend> friends = dbf.getAllNondeletedFriends();
        assertTrue(friends.size() == 0);
    }

    @Test
    public void shouldUpdateFriend() {
        Friend friend = new Friend("Stefan", false);
        dbf.insert(friend);
        List<Friend> friends_loaded = dbf.getAllFriends();
        friends_loaded.get(0).setName("Klemens");
        dbf.updateFriend(friends_loaded.get(0));
        List<Friend> friends = dbf.getAllFriends();
        assertTrue(friends.get(0).getName() != "Stefan");
    }

    @Test
    public void useAppContext () throws Exception {
// Context of the app under test.
        Context appContext = InstrumentationRegistry . getTargetContext();
        assertEquals( "at.sw2017.pocketdiary" , appContext . getPackageName ());
    }

    @Test
    public void shouldShowAlert() {
        String name = "Stefan";
        onView(withId(R.id.editText)).perform(typeText(name));
        onView(withId(R.id.button)).perform(click());
        onView(allOf(withText(name))).perform(click());
        onView(withText("Edit a friend."));
    }

    @Test
    public void shouldShowAlertAndUpdate() {
        String name = "Stefan";
        onView(withId(R.id.editText)).perform(typeText(name));
        onView(withId(R.id.button)).perform(click());
        onView(allOf(withText(name))).perform(click());
        onView(withText("Update friend")).perform(click());
        onView(allOf(withText(name))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowAlertAndCancel() {
        String name = "Stefan";
        onView(withId(R.id.editText)).perform(typeText(name));
        onView(withId(R.id.button)).perform(click());
        onView(allOf(withText(name))).perform(click());
        onView(withText("Cancel")).perform(click());
        onView(allOf(withText(name))).check(matches(isDisplayed()));
    }
    
}