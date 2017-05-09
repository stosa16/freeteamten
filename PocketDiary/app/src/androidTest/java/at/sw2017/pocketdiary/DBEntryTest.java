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
}
