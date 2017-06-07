package at.sw2017.pocketdiary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import at.sw2017.pocketdiary.business_objects.CustomDate;

import static junit.framework.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class BOCustomDateTest {

    @Test
    public void testGetCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        CustomDate cd = new CustomDate();
        String cd_current = cd.getCurrentDate();

        assertEquals(formattedDate, cd_current);
    }

    @Test
    public void testGetCurrentDay(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String formattedDate = df.format(c.getTime());

        CustomDate cd = new CustomDate();
        int cd_current = cd.getCurrentDay();

        assertEquals(Integer.parseInt(formattedDate), cd_current);
    }

    @Test
    public void testGetCurrentMonth(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String formattedDate = df.format(c.getTime());

        CustomDate cd = new CustomDate();
        int cd_current = cd.getCurrentMonth() + 1;

        assertEquals(Integer.parseInt(formattedDate), cd_current);
    }

    @Test
    public void testGetCurrentYear(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String formattedDate = df.format(c.getTime());

        CustomDate cd = new CustomDate();
        int cd_current = cd.getCurrentYear();

        assertEquals(Integer.parseInt(formattedDate), cd_current);
    }
}
