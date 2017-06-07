package at.sw2017.pocketdiary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.pocketdiary.business_objects.Friend;

import static junit.framework.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class BOFriendTest {

    @Test
    public void testFriendInitWithoutParameters(){
        int id = 1;
        String name = "Friend";
        boolean deleted = false;

        Friend friend = new Friend();
        friend.setId(id);
        friend.setName(name);
        friend.setDeleted(deleted);

        assertEquals(id, friend.getId());
        assertEquals(name, friend.getName());
        assertEquals(deleted, friend.isDeleted());
    }

    @Test
    public void testFriendWithTwoParameters(){
        String name = "Friend";
        boolean deleted = false;

        Friend friend = new Friend(name, deleted);

        assertEquals(name, friend.getName());
        assertEquals(deleted, friend.isDeleted());
    }

    @Test
    public void testFriendInitWithAllParameters() {
        int id = 1;
        String name = "Friend";
        boolean deleted = false;

        Friend friend = new Friend(id, name, deleted);

        assertEquals(id, friend.getId());
        assertEquals(name, friend.getName());
        assertEquals(deleted, friend.isDeleted());
    }
}
