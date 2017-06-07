package at.sw2017.pocketdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import at.sw2017.pocketdiary.database_access.DBHandler;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DBHandlerTest {

    DBHandler dbh;

    @Test
    public void checkIfDBCreated(){
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        File dbtest =new File("/data/data/at.sw2017.pocketdiary/databases/"+DBHandler.DATABASE_NAME);
        assertEquals(true, dbtest.exists());
    }

    @Test
    public void checkIfDBUpgraded(){
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        File dbtest =new File("/data/data/at.sw2017.pocketdiary/databases/"+DBHandler.DATABASE_NAME);
        assertEquals(true, dbtest.exists());

        dbh = new DBHandler(context, 2);
        assertEquals(true, dbtest.exists());

    }
}
