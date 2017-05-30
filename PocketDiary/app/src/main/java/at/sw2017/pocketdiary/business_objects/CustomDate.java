package at.sw2017.pocketdiary.business_objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by marku on 26.05.2017.
 */

public class CustomDate {

    public CustomDate(){

    }

    public String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public int getCurrentDay(){
        return Integer.parseInt(getCurrentDate().split("-")[2]);
    }

    public int getCurrentMonth(){
        return Integer.parseInt(getCurrentDate().split("-")[1])-1;
    }

    public int getCurrentYear(){
        return Integer.parseInt(getCurrentDate().split("-")[0]);
    }
}
