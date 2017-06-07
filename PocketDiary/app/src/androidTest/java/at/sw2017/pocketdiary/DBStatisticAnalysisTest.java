package at.sw2017.pocketdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBStatistic;
import at.sw2017.pocketdiary.database_access.DBStatisticAnalysis;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DBStatisticAnalysisTest {

    DBHandler dbh;
    DBStatisticAnalysis dbsa;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbsa = new DBStatisticAnalysis(context);
        TestHelper.initCategories(context);
        TestHelper.initAddress(context);
        TestHelper.createTestEntryBasic(context);
        TestHelper.initFriends(context);
    }

    @After
    public void tearDown() {
        dbh.close();
        dbsa.close();
    }

    @Test
    public void testGetEntriesByMainCategoryId(){
        Entry entry = dbsa.getEntriesByMainCategoryId(1).get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());

    }

    @Test
    public void testGetEntriesBySubCategoryId(){
        Entry entry = dbsa.getEntriesBySubCategoryId(4).get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());

    }

    @Test
    public void testGetEntriesBySearchTerm(){
        Entry entry = dbsa.getEntriesBySearchTerm("test").get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());

    }

    @Test
    public void testGetEntriesBetweenDates(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 4, 5, 0, 0, 0);
        Date date_from = calendar.getTime();
        calendar.set(2017, 5, 5, 0, 0, 0);
        Date date_til = calendar.getTime();

        Entry entry = dbsa.getEntriesBetweenDates(date_from, date_til).get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());

    }

    @Test
    public void testGetEntryByMainCatName(){
        Entry entry = dbsa.getEntriesBySearchTerm("Sport").get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());

    }

    @Test
    public void testGetEntryBySubCatName(){
        Entry entry = dbsa.getEntriesBySearchTerm("Walking").get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());

    }

    @Test
    public void testGetEntryByAddressPoi(){
        Entry entry = dbsa.getEntriesBySearchTerm("Poi").get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());
    }

    @Test
    public void testGetEntryByAddressCity(){
        Entry entry = dbsa.getEntriesBySearchTerm("City").get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());
    }

    @Test
    public void testGetEntryByAddressCountry(){
        Entry entry = dbsa.getEntriesBySearchTerm("Country").get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());
    }

    @Test
    public void testGetEntryByAddressZip(){
        Entry entry = dbsa.getEntriesBySearchTerm("0000").get(0);

        assertEquals(1, entry.getId());
        assertEquals("Test", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(1), entry.getAddressId());
    }

    @Test
    public void testGetEntryByFriendName(){
        Entry entry = dbsa.getEntriesBySearchTerm("Hermann").get(0);

        assertEquals(2, entry.getId());
        assertEquals("Test_Street", entry.getTitle());
        assertEquals("This is a test!", entry.getDescription());
        assertEquals(1, entry.getMainCategoryId());
        assertEquals(4, entry.getSubCategoryId());
        assertEquals(new Integer(2), entry.getAddressId());
    }
}
