package at.sw2017.pocketdiary;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;


public class ReverseGeocoder {

    public at.sw2017.pocketdiary.business_objects.Address getAddress(double longitude, double latitude, Geocoder geocoder) throws IOException {

        at.sw2017.pocketdiary.business_objects.Address entry_address = new at.sw2017.pocketdiary.business_objects.Address();

        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

        if (addresses.size() != 0) {
            Address current_address = addresses.get(0);
            StringBuilder address_string = new StringBuilder();
            for (int i = 0; i < current_address.getMaxAddressLineIndex(); i++) {
                address_string.append(current_address.getAddressLine(i)).append("\n");
            }
            entry_address.setStreet(address_string.toString());
            entry_address.setZip(current_address.getPostalCode());
            entry_address.setCity(current_address.getLocality());
            entry_address.setCountry(current_address.getCountryName());
            entry_address.setLatitude(latitude);
            entry_address.setLongitude(longitude);
            return entry_address;
        } else {
            return entry_address;
        }
    }

}
