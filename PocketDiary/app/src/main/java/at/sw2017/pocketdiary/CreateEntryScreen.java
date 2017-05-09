package at.sw2017.pocketdiary;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBHandler;

public class CreateEntryScreen extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText input_event_title;
    private Spinner input_main_category;
    private Spinner input_sub_category;
    private EditText input_description;

    private String empty_spinner_text = "Select Category";
    private Date entry_date = null;
    private Address entry_address = null;

    private static final int FINE_LOCATION_REQUEST = 3;
    private static final int COARSE_LOCATION_REQUEST = 4;

    TextView badge_camera;
    TextView badge_address;
    TextView badge_date;
    TextView badge_friends;

    List<String> strings_maincategories = new ArrayList<>();
    List<Category> maincategories = new ArrayList<>();
    List<Category> subcategories = new ArrayList<>();
    List<String> strings_subcategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry_screen);
        DBHandler db = new DBHandler(this);
        initCategories();
        initDateButton();
        initBadges();
    }

    public void checkLocationPermissions(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, COARSE_LOCATION_REQUEST);
            } else {
                initLocationButton();
            }
        } else {
            initLocationButton();
        }
    }

    private void initLocationButton() {
        ImageButton location_button = (ImageButton) this.findViewById(R.id.btn_location);
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsLocation gps = new GpsLocation(CreateEntryScreen.this);
                if (gps.canGetLocation()) {
                    double longitude = gps.getLongitude();
                    double latitude = gps.getLatitude();
                    Geocoder geocoder = new Geocoder(CreateEntryScreen.this, Locale.ENGLISH);
                    ReverseGeocoder reverseGeocoder = new ReverseGeocoder();
                    try {
                        entry_address = reverseGeocoder.getAddress(longitude, latitude, geocoder);
                    } catch (IOException e) {
                        entry_address = new Address(longitude, latitude);
                        e.printStackTrace();
                    }
                    Helper.updateBadgeVisibility(badge_address, true);
                    Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Addresse: " + entry_address.getStreet() + "Land: " + entry_address.getCountry(), Toast.LENGTH_SHORT).show();
                } else {
                    gps.showSettingsAlert();
                }
            }
        });
        location_button.callOnClick();
    }


    public void initDateButton() {
        Calendar currentDate = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, CreateEntryScreen.this, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        (findViewById(R.id.btn_calendar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();
                    }
                });
    }

    private void showFeatureDisabledDialog() {
        Toast.makeText(getApplicationContext(), "This feature was disabled due to missing permissions!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grant_results) {
        switch (requestCode) {
            case COARSE_LOCATION_REQUEST:
            case FINE_LOCATION_REQUEST: {
                ImageButton location_button = (ImageButton) this.findViewById(R.id.btn_location);
                if (grant_results.length > 0
                        && grant_results[0] == PackageManager.PERMISSION_GRANTED && grant_results[1] == PackageManager.PERMISSION_GRANTED) {
                    initLocationButton();
                } else {
                    location_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showFeatureDisabledDialog();
                        }
                    });
                    location_button.performClick();
                }
                return;
            }
        }
    }

    private void initBadges() {
        badge_camera = (TextView) findViewById(R.id.badge_camera);
        badge_address = (TextView) findViewById(R.id.badge_address);
        badge_date = (TextView) findViewById(R.id.badge_date);
        badge_friends = (TextView) findViewById(R.id.badge_friends);
        Helper.updateBadgeVisibility(badge_camera, false);
        Helper.updateBadgeVisibility(badge_address, false);
        Helper.updateBadgeVisibility(badge_date, false);
        Helper.updateBadgeVisibility(badge_friends, false);
    }

    private void initCategories() {
        DBCategory dbc = new DBCategory(this);
        if (dbc.getAllCategories().size() == 0) {
            Helper.initCategories(this);
        }
        maincategories = dbc.getMainCategories();
        strings_maincategories.add(empty_spinner_text);
        for (Category temp_cat : maincategories) {
            strings_maincategories.add(temp_cat.getName());
        }

        input_main_category = (Spinner) findViewById(R.id.out_category);
        ArrayAdapter<String> main_spinner = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, strings_maincategories);

        input_main_category.setAdapter(main_spinner);
        input_main_category.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        Category selected_category = getCategoryByName(maincategories, input_main_category.getSelectedItem().toString());
                        if (selected_category != null) {
                            fillSubCategories(selected_category.getId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
        fillSubCategories(-1);
    }

    private void fillSubCategories(int parent_id) {
        DBCategory dbCategory = new DBCategory(this);
        strings_subcategories = new ArrayList<>();
        subcategories = dbCategory.getSubCategories(parent_id);
        strings_subcategories.add(empty_spinner_text);
        for (Category temp_cat : subcategories) {
            strings_subcategories.add(temp_cat.getName());
            System.out.println(temp_cat.getName());
        }
        input_sub_category = (Spinner) findViewById(R.id.input_subcategory);
        ArrayAdapter<String> sub_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strings_subcategories);
        input_sub_category.setAdapter(sub_spinner);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        entry_date = calendar.getTime();
        Helper.updateBadgeVisibility(badge_date, true);
    }

    private Category getCategoryByName(List<Category> categories, String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public void saveEvent(View view) {
        input_event_title = (EditText) findViewById(R.id.out_title);
        String title = input_event_title.getText().toString();
        if (title.equals("")) {
            Toast.makeText(CreateEntryScreen.this, "Please define a Title!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        input_description = (EditText) findViewById(R.id.input_description);
        String description = input_description.getText().toString();
        input_main_category = (Spinner) findViewById(R.id.out_category);
        Category main_category = getCategoryByName(maincategories, input_main_category.getSelectedItem().toString());
        if (main_category == null) {
            Toast.makeText(CreateEntryScreen.this, "Please select a Main Category!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        input_sub_category = (Spinner) findViewById(R.id.input_subcategory);
        Category sub_category = getCategoryByName(subcategories, input_sub_category.getSelectedItem().toString());
        if (sub_category == null) {
            Toast.makeText(CreateEntryScreen.this, "Please select a Subcategory!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Entry entry = new Entry();
        entry.setTitle(title);
        entry.setDescription(description);
        entry.setMainCategoryId(main_category.getId());
        entry.setSubCategoryId(sub_category.getId());
        if (entry_date != null) {
            entry.setDate(entry_date);
        } else {
            Toast.makeText(CreateEntryScreen.this, "Please select a Date!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (entry_address != null) {
            DBAddress dba = new DBAddress(this);
            long id;
            if (entry_address.getStreet() != null) {
                id = dba.insert(entry_address);
            } else {
                id = dba.insertLatitudeLongitude(entry_address);
            }
            entry_address.setId((int) id);
            entry.setAddress(entry_address);
        }
        insertEntryToDatabase(entry);
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
    }

    public long insertEntryToDatabase(Entry entry) {
        DBEntry db_entry = new DBEntry(this);
        long id = db_entry.insert(entry);
        return id;
    }

    public void handleCancelButton(View view) {
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
    }
}