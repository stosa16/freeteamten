package at.sw2017.pocketdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBHandler;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DBAddressTest {
    private DBHandler dbh;
    private DBAddress dba;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dba = new DBAddress(context);
    }

    @After
    public void tearDown() throws Exception {
        dbh.close();
        dba.close();
    }

    @Test
    public void shouldAddAddress() throws Exception {
        Address address = new Address("Test", "Alte Poststraß4 147", "8020", "Graz", "Austria", 47.069718, 15.409874);
        long id = dba.insert(address);
        assertTrue(id > 0);
        Address loaded_address = dba.getAddress((int)id);
        assertTrue(loaded_address.getPoi().equals(address.getPoi()));
        assertTrue(loaded_address.getStreet().equals(address.getStreet()));
        assertTrue(loaded_address.getZip().equals(address.getZip()));
        assertTrue(loaded_address.getCity().equals(address.getCity()));
        assertTrue(loaded_address.getCountry().equals(address.getCountry()));
        long address_latitude = Double.doubleToLongBits(loaded_address.getLatitude());
        long address_longitude = Double.doubleToLongBits(loaded_address.getLongitude());
        long address_loaded_latitute = Double.doubleToLongBits(loaded_address.getLatitude());
        long address_loaded_longitude = Double.doubleToLongBits(loaded_address.getLongitude());
        assertTrue(address_latitude == address_loaded_latitute);
        assertTrue(address_longitude == address_loaded_longitude);
    }

    @Test
    public void shouldAddAddressLatitudeLongitude() throws Exception {
        Address address = new Address(47.000, 49.034);
        long id = dba.insertLatitudeLongitude(address);
        assertTrue(id > 0);
        Address loaded_address = dba.getAddress((int)id);
        long address_latitude = Double.doubleToLongBits(loaded_address.getLatitude());
        long address_longitude = Double.doubleToLongBits(loaded_address.getLongitude());
        long address_loaded_latitute = Double.doubleToLongBits(loaded_address.getLatitude());
        long address_loaded_longitude = Double.doubleToLongBits(loaded_address.getLongitude());
        assertTrue(address_latitude == address_loaded_latitute);
        assertTrue(address_longitude == address_loaded_longitude);
    }
}
