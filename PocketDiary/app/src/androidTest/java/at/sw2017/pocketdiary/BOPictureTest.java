package at.sw2017.pocketdiary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.pocketdiary.business_objects.Picture;

import static junit.framework.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class BOPictureTest {

    @Test
    public void testPictureInitWithoutParameters(){
        int id = 1;
        String fp = "./filepath";
        String name = "Picture";

        Picture pic = new Picture();
        pic.setId(id);
        pic.setFilePath(fp);
        pic.setName(name);

        assertEquals(id, pic.getId());
        assertEquals(fp, pic.getFilePath());
        assertEquals(name, pic.getName());
    }

    @Test
    public void testPictureInitWithTwoParameters() {
        String fp = "./filepath";
        String name = "Picture";

        Picture pic = new Picture(fp, name);

        assertEquals(fp, pic.getFilePath());
        assertEquals(name, pic.getName());
    }


}
