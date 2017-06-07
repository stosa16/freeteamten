package at.sw2017.pocketdiary;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class GpsLocationInstrumentedTest {

    private Context context;

    @Rule
    public ActivityTestRule<CreateEntryScreen> mActivityRule = new ActivityTestRule<>(CreateEntryScreen.class);


    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        Intents.init();
    }

    @After
    public void release() {
        Intents.release();
    }

    @Test
    public void checkGetLongitudeAndLatitude() {
        GpsLocation gps_location = new GpsLocation(context);
        Location current_location = new Location("A1");
        current_location.setLongitude(15);
        current_location.setLatitude(13);
        assertTrue(gps_location.getLongitude() == 0);
        assertTrue(gps_location.getLatitude() == 0);
        gps_location.current_location = current_location;
        assertTrue(gps_location.getLongitude() == current_location.getLongitude());
        assertTrue(gps_location.getLatitude() == current_location.getLatitude());
    }

    @Test
    public void checkCanGetLocation() {
        GpsLocation gps_location = new GpsLocation(context);
        gps_location.canGetLocation = false;
        assertTrue(gps_location.canGetLocation() == gps_location.canGetLocation);
        gps_location.canGetLocation = true;
        assertTrue(gps_location.canGetLocation() == gps_location.canGetLocation);
    }

    @Test
    public void checkOverrideMethods() {
        GpsLocation gps_location = new GpsLocation(context);
        assertTrue(gps_location.onBind(new Intent("intent")) == null);
        gps_location.onStatusChanged("test", 1, Bundle.EMPTY);
        gps_location.onProviderEnabled("enabled");
        gps_location.onProviderDisabled("disabled");
    }

    @Test
    public void checkOnLocationChanged(){
        GpsLocation gps_location = new GpsLocation(context);
        Location current_location = new Location("A1");
        current_location.setLongitude(15);
        current_location.setLatitude(13);
        gps_location.onLocationChanged(current_location);
        assertTrue(gps_location.current_location == current_location);
    }


}
