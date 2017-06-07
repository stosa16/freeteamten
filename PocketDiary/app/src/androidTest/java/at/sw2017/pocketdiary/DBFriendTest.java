package at.sw2017.pocketdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBFriend;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DBFriendTest {

    DBHandler dbh;
    DBFriend dbf;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbf = new DBFriend(context);
    }

    @After
    public void tearDown() {
        dbh.close();
        dbf.close();
    }

    @Test
    public void testInsert() {
        String name = "Hermann";
        Friend friend = new Friend(name);
        long id = dbf.insert(friend);

        Friend db_friend = dbf.getAllFriends().get(0);

        assertEquals(id, db_friend.getId());
        assertEquals(name, db_friend.getName());
        assertEquals(false, db_friend.isDeleted());
    }

    @Test
    public void testUpdate() {
        String name = "Hermann";
        Friend friend = new Friend(name);
        long id = dbf.insert(friend);

        Friend db_friend = dbf.getAllFriends().get(0);

        assertEquals(id, db_friend.getId());
        assertEquals(name, db_friend.getName());
        assertEquals(false, db_friend.isDeleted());

        String n_name = "Tim";
        db_friend.setName(n_name);

        dbf.updateFriend(db_friend);

        Friend db_up_friend = dbf.getAllFriends().get(0);

        assertEquals(id, db_up_friend.getId());
        assertEquals(n_name, db_up_friend.getName());
        assertEquals(false, db_up_friend.isDeleted());

    }

    @Test
    public void testGetFriendById(){
        String name = "Hermann";
        Friend friend = new Friend(name);
        long id = dbf.insert(friend);

        Friend db_friend = dbf.getFriend((int) id);

        assertEquals(id, db_friend.getId());
        assertEquals(name, db_friend.getName());
        assertEquals(false, db_friend.isDeleted());
    }

    @Test
    public void testGetAllNonDeletedFriends() {
        String name = "Hermann";
        Friend friend = new Friend(name);
        long id = dbf.insert(friend);

        Friend db_friend = dbf.getAllNondeletedFriends().get(0);

        assertEquals(id, db_friend.getId());
        assertEquals(name, db_friend.getName());
        assertEquals(false, db_friend.isDeleted());
    }
}
