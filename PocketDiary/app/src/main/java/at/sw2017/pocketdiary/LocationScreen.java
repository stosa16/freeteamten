package at.sw2017.pocketdiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.database_access.DBAddress;

public class LocationScreen extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_screen);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();
        DBAddress dbAddress = new DBAddress(this);
        int address_id = Integer.parseInt(intent.getStringExtra("address_id"));
        Address address = dbAddress.getAddress(address_id);
        double longitude = address.getLongitude();
        double latitude = address.getLatitude();
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(address.getStreet()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14.5f));
    }
}
