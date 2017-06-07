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
        byte[] pic = new byte[1];
        String pin = "0123";
        String pin_active = "1";

        UserSetting us = new UserSetting();

        us.setId(id);
        us.setUserName(name);
        us.setPicture(pic);
        us.setPin(pin);
        us.setPinActive(pin_active);

        assertEquals(id, us.getId());
        assertEquals(name, us.getUserName());
        assertEquals(pic, us.getPicture());
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
    public void testUSInitWithThreeParameters(){
        int id = 1;
        String pin = "0123";
        String pin_active = "1";

        UserSetting us = new UserSetting(id, pin, pin_active);

        assertEquals(id, us.getId());
        assertEquals(pin, us.getPin());
        assertEquals(pin_active, us.isPinActive());
    }
}
