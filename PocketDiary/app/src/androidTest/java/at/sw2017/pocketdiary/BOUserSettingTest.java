package at.sw2017.pocketdiary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.pocketdiary.business_objects.UserSetting;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BOUserSettingTest {

    @Test
    public void testUSInitWithoutParameters(){
        int id = 1;
        String name = "User";
        String pic_name = "Picture";
        String file_path = "./file";
        String pin = "0123";
        String pin_active = "1";

        UserSetting us = new UserSetting();

        us.setId(id);
        us.setUserName(name);
        us.setPicturename(pic_name);
        us.setFilePath(file_path);
        us.setPin(pin);
        us.setPinActive(pin_active);

        assertEquals(id, us.getId());
        assertEquals(name, us.getUserName());
        assertEquals(pic_name, us.getPicturename());
        assertEquals(file_path, us.getFilePath());
        assertEquals(pin, us.getPin());
        assertEquals(pin_active, us.isPinActive());
    }

    @Test
    public void testUSInitWithNameParameter(){
        String name = "User";

        UserSetting us = new UserSetting(name);

        assertEquals(name, us.getUserName());
    }

    @Test
    public void testUSInitWithAllParameters(){
        int id = 1;
        String pin = "0123";
        String pin_active = "1";
        String pic_name = "Picture";
        String file_path = "./file";

        UserSetting us = new UserSetting(id, pin, pin_active, file_path, pic_name);

        assertEquals(id, us.getId());
        assertEquals(pin, us.getPin());
        assertEquals(pin_active, us.isPinActive());
        assertEquals(file_path, us.getFilePath());
        assertEquals(pic_name, us.getPicturename());
    }
}
