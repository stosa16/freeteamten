package at.sw2017.pocketdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Statistic;
import at.sw2017.pocketdiary.business_objects.UserSetting;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBUserSetting;

import static junit.framework.Assert.assertEquals;

/**
 * Created by schus on 30.05.2017.
 */

public class UserSettingsUnitTest {

    DBHandler dbh;
    DBUserSetting dbus;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbus = new DBUserSetting(context);
        TestHelper.initCategories(context);
    }

    @After
    public void tearDown() {
        dbh.close();
        dbus.close();
    }

    @Test
    public void testInsert(){
        String pin = "123456";
        String isPinActive = "1";
        String userName = "Anna";
        String filePath = "images/1234.jpg";
        String pictureName = "1234";

        UserSetting userSetting = new UserSetting();
        userSetting.setId(1);
        userSetting.setUserName(userName);
        userSetting.setPin(pin);
        userSetting.setPinActive(isPinActive);
        userSetting.setFilePath(filePath);
        userSetting.setPicturename(pictureName);

        dbus.insert(userSetting);
        List<UserSetting> temp = dbus.getUserSetting(1);
        UserSetting setting = temp.get(0);

        String name_db = setting.getUserName();
        String pin_db = setting.getPin();
        String path_db = setting.getFilePath();
        String pic_name_db = setting.getPicturename();

        assertEquals(userName, name_db);
        assertEquals(pin, pin_db);
        assertEquals(filePath, path_db);
        assertEquals(pictureName, pic_name_db);
    }

    @Test
    public void testUpdate(){
        testInsert();
        String pin = "1234567";
        String userName = "Maria";
        String filePath = "images/12345.jpg";
        String pictureName = "12345";

        UserSetting userSetting = new UserSetting();
        userSetting.setId(1);
        userSetting.setUserName(userName);
        userSetting.setPin(pin);
        userSetting.setFilePath(filePath);
        userSetting.setPicturename(pictureName);

        dbus.update(userSetting, 1);
        List<UserSetting> temp = dbus.getUserSetting(1);
        UserSetting setting = temp.get(0);

        String name_db = setting.getUserName();
        String pin_db = setting.getPin();
        String path_db = setting.getFilePath();
        String pic_name_db = setting.getPicturename();

        assertEquals(userName, name_db);
        assertEquals(pin, pin_db);
        assertEquals(filePath, path_db);
        assertEquals(pictureName, pic_name_db);
    }
}
