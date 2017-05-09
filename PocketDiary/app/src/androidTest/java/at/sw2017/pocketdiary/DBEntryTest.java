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
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBPicture;

import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class DBEntryTest {
    private DBHandler dbh;
    private DBEntry dbe;
    private DBAddress dba;
    private DBPicture dbp;
    Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbe = new DBEntry(context);
        dba = new DBAddress(context);
        dbp = new DBPicture(context);
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
    public void shouldAddEntryWithPicture() throws Exception {
        Entry entry = new Entry("Test", 1, 2, Calendar.getInstance().getTime(), "Das ist ein Test!");
        String file_path = "/Test/";
        String image_name = "test.jpeg";
        Picture picture = new Picture(file_path, image_name);
        long picture_id = dbp.insert(picture);
        picture.setId((int) picture_id);
        entry.addPicture(picture);
        long id = dbe.insert(entry);
        assertTrue(id > 0);
        Entry entry_loaded = Helper.getEntryComplete(context, (int) id);
        Picture picture_loaded = entry_loaded.getPictures().get(0);
        assertTrue(picture_loaded.getFilePath().equals(file_path));
        assertTrue(picture_loaded.getName().equals(image_name));
    }
}
