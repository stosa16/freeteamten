package at.sw2017.pocketdiary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.pocketdiary.business_objects.EntryFriend;

import static junit.framework.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class BOEntryFriendTest {

    @Test
    public void testEntryFriendInitWithoutParameters(){
        int f_id, e_id;
        f_id = e_id = 1;

        EntryFriend ef = new EntryFriend();
        ef.setEventId(e_id);
        ef.setFriendId(f_id);

        assertEquals(f_id, ef.getFriendId());
        assertEquals(e_id, ef.getEventId());
    }

    @Test
    public void testEntryFriendInitWithAllParameters(){
        int f_id, e_id;
        f_id = e_id = 1;

        EntryFriend ef = new EntryFriend(f_id, e_id);

        assertEquals(f_id, ef.getFriendId());
        assertEquals(e_id, ef.getEventId());
    }
}
