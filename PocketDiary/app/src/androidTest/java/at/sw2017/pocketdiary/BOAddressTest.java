package at.sw2017.pocketdiary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.pocketdiary.business_objects.Address;

import static junit.framework.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class BOAddressTest {

    @Test
    public void testAddressInitWithoutParameters(){
        Address address = new Address();

        int id = 1;
        String poi = "Poi";
        String street = "Street";
        String zip = "0000";
        String country = "Country";
        String city = "City";
        Double lat_lng = 00.00;
        address.setId(1);
        address.setPoi(poi);
        address.setStreet(street);
        address.setZip(zip);
        address.setCountry(country);
        address.setCity(city);
        address.setLatitude(lat_lng);
        address.setLongitude(lat_lng);

        assertEquals(id, address.getId());
        assertEquals(poi, address.getPoi());
        assertEquals(street, address.getStreet());
        assertEquals(city, address.getCity());
        assertEquals(zip, address.getZip());
        assertEquals(country, address.getCountry());
        assertEquals(lat_lng, address.getLatitude());
        assertEquals(lat_lng, address.getLongitude());

    }

    @Test
    public void testAddressInitWithLatLng(){
        Double lat_lng = 00.00;
        Address address = new Address(lat_lng, lat_lng);
        assertEquals(lat_lng, address.getLongitude());
        assertEquals(lat_lng, address.getLatitude());
    }

    @Test
    public void testAddressInitWithLatLngStreet(){
        Double lat_lng = 00.00;
        String street = "Street";
        Address address = new Address(street, lat_lng, lat_lng);
        assertEquals(lat_lng, address.getLongitude());
        assertEquals(lat_lng, address.getLatitude());
        assertEquals(street, address.getStreet());
    }

    @Test
    public void testAddressInitWithAllParameters(){
        int id = 1;
        String poi = "Poi";
        String street = "Street";
        String zip = "0000";
        String country = "Country";
        String city = "City";
        Double lat_lng = 00.00;

        Address address = new Address(id, poi, street, zip, city, country, lat_lng, lat_lng);

        assertEquals(id, address.getId());
        assertEquals(poi, address.getPoi());
        assertEquals(street, address.getStreet());
        assertEquals(city, address.getCity());
        assertEquals(zip, address.getZip());
        assertEquals(country, address.getCountry());
        assertEquals(lat_lng, address.getLatitude());
        assertEquals(lat_lng, address.getLongitude());
    }

    @Test
    public void testAddressInitWithoutId(){
        String poi = "Poi";
        String street = "Street";
        String zip = "0000";
        String country = "Country";
        String city = "City";
        Double lat_lng = 00.00;

        Address address = new Address(poi, street, zip, city, country, lat_lng, lat_lng);

        assertEquals(poi, address.getPoi());
        assertEquals(street, address.getStreet());
        assertEquals(city, address.getCity());
        assertEquals(zip, address.getZip());
        assertEquals(country, address.getCountry());
        assertEquals(lat_lng, address.getLatitude());
        assertEquals(lat_lng, address.getLongitude());
    }
}
