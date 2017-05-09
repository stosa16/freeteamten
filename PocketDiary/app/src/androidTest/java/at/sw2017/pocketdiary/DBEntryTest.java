package at.sw2017.pocketdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class DBEntryTest {
    private DBHandler dbh;
    private DBEntry dbe;
    private DBAddress dba;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbe = new DBEntry(context);
        dba = new DBAddress(context);
    }

    @After
    public void tearDown() throws Exception {
        dbh.close();
        dbe.close();
        dba.close();
    }

    @Test
    public void shouldAddEntryBasic() throws Exception {
        Entry entry = new Entry("Test", 1, 2, (Date) Calendar.getInstance().getTime(), "Das ist ein Test!");
        long id = dbe.insert(entry);
        assertTrue(id > 0);
        Entry entry_loaded = dbe.getEntry((int) id);
        assertTrue(entry.getDescription().equals(entry_loaded.getDescription()));
        assertTrue(entry.getTitle().equals(entry_loaded.getTitle()));
        assertTrue(entry.getMainCategoryId() == (entry_loaded.getMainCategoryId()));
        assertTrue(entry.getSubCategoryId() == (entry_loaded.getSubCategoryId()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertTrue(dateFormat.format(entry.getDate()).equals(dateFormat.format(entry_loaded.getDate())));
    }

    @Test
    public void shouldAddEntryLocation() throws Exception {
        Address address = new Address(123.45, 67.89);
        long id_add = dba.insert(address);
        assertTrue(id_add > 0);
        Entry entry = new Entry("Test", 1, 2, (Date) Calendar.getInstance().getTime(), "Das ist ein Test!", address);
        long id = dbe.insert(entry);
        assertTrue(id > 0);
        Entry entry_loaded = dbe.getEntry((int) id);
        assertTrue(entry.getDescription().equals(entry_loaded.getDescription()));
        assertTrue(entry.getTitle().equals(entry_loaded.getTitle()));
        assertTrue(entry.getMainCategoryId() == (entry_loaded.getMainCategoryId()));
        assertTrue(entry.getSubCategoryId() == (entry_loaded.getSubCategoryId()));
        long address_latitude = Double.doubleToLongBits(address.getLatitude());
        long address_longitude = Double.doubleToLongBits(address.getLongitude());
        long entry_loaded_latitute = Double.doubleToLongBits(entry.getAddress().getLatitude());
        long entry_loaded_longitude = Double.doubleToLongBits(entry.getAddress().getLongitude());
        assertTrue(address_latitude == entry_loaded_latitute);
        assertTrue(address_longitude == entry_loaded_longitude);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertTrue(dateFormat.format(entry.getDate()).equals(dateFormat.format(entry_loaded.getDate())));
    }

    @Test
    public void shouldGetAllEntries(){
        Entry entry = new Entry("Test", 1, 2, (Date) Calendar.getInstance().getTime(), "Das ist ein Test!");
        dbe.insert(entry);
        List<Entry> entries_loaded = dbe.getAllEntries();
        Entry entry_loaded = entries_loaded.get(0);
        assertTrue(entry.getDescription().equals(entry_loaded.getDescription()));
        assertTrue(entry.getTitle().equals(entry_loaded.getTitle()));
        assertTrue(entry.getMainCategoryId() == (entry_loaded.getMainCategoryId()));
        assertTrue(entry.getSubCategoryId() == (entry_loaded.getSubCategoryId()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertTrue(dateFormat.format(entry.getDate()).equals(dateFormat.format(entry_loaded.getDate())));
    }
}
