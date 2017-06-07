package at.sw2017.pocketdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBPicture;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DBPictureTest {
    private DBHandler dbh;
    private DBPicture dbp;
    private DBEntry dbe;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbp = new DBPicture(context);
        dbe = new DBEntry(context);
    }

    @After
    public void tearDown() throws Exception {
        dbh.close();
        dbp.close();
        dbe.close();
    }

    @Test
    public void callEmptyOverrides() throws Exception {
        dbp.onCreate(null);
        dbp.onUpgrade(null, 0, 0);
    }

    @Test
    public void shouldGetNoPicture() {
        Picture loaded_picture = dbp.getPicture(0);
        assertTrue(loaded_picture == null);
    }

    @Test
    public void shouldAddPicture() {
        String test_path = "/Testpath/";
        String test_name = "picture.jpeg";
        Picture picture = new Picture();
        picture.setFilePath(test_path);
        picture.setName(test_name);
        long id = dbp.insert(picture);
        assertTrue(id > 0);
        Picture loaded_picture = dbp.getPicture((int) id);
        assertTrue(loaded_picture.getFilePath().equals(picture.getFilePath()));
        assertTrue(loaded_picture.getName().equals(picture.getName()));
    }

    @Test
    public void shouldGetAllImagesOfEntry() {
        Entry entry = new Entry();
        entry.setTitle("Test");
        entry.setMainCategoryId(1);
        entry.setSubCategoryId(2);
        List<Picture> pictures = new ArrayList<>();
        Picture picture1 = new Picture("/Test/Test1/", "picture1.jpeg");
        long picture1_id = dbp.insert(picture1);
        picture1.setId((int) picture1_id);
        Picture picture2 = new Picture("/Test/Test2/", "picture2.jpeg");
        long picture2_id = dbp.insert(picture2);
        picture2.setId((int) picture2_id);
        Picture picture3 = new Picture("/Test/Test3/", "picture3.jpeg");
        long picture3_id = dbp.insert(picture3);
        picture3.setId((int) picture3_id);
        Picture picture4 = new Picture("/Test/Test4/", "picture4.jpeg");
        long picture4_id = dbp.insert(picture4);
        picture4.setId((int) picture4_id);
        pictures.add(picture1);
        pictures.add(picture2);
        pictures.add(picture3);
        pictures.add(picture4);
        entry.setPictures(pictures);
        long id = dbe.insert(entry);
        List<Picture> loaded_pictures = dbp.getPicturesOfEntry((int) id);
        assertTrue(loaded_pictures.size() == 4);
    }
}
