package at.sw2017.pocketdiary;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBFriend;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBPicture;

public class CreateEntryScreen extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText input_event_title;
    private Spinner input_main_category;
    private Spinner input_sub_category;
    private EditText input_description;
    private DatePickerDialog datePickerDialog;

    public int entry_id;

    private String empty_spinner_text = "Select Category";
    public Address entry_address = null;
    private Date entry_date = null;
    public List<String> entry_picture_paths = new ArrayList<>();

    private Entry entry = new Entry();
    private static final int CAMERA_REQUEST = 1;
    private static final int WRITE_STORAGE_REQUEST = 2;
    private static final int FINE_LOCATION_REQUEST = 3;
    private static final int COARSE_LOCATION_REQUEST = 4;
    private static final int PICK_IMAGE_REQUEST = 5;
    private static final int DELETE_IMAGE_REQUEST = 6;

    TextView badge_camera;
    TextView badge_address;
    TextView badge_date;
    TextView badge_friends;

    List<String> strings_maincategories = new ArrayList<>();
    List<Category> maincategories = new ArrayList<>();
    List<Category> subcategories = new ArrayList<>();
    List<String> strings_subcategories = new ArrayList<>();
    List<String> items = new ArrayList<String>();
    List<Friend> friends = new ArrayList<Friend>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        entry_id = Integer.parseInt(intent.getStringExtra("entry_id"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry_screen);
        DBHandler db = new DBHandler(this);
        initCategories();
        initDateButton();
        initBadges();
        initFriends();
        if (entry_id != 0) {
            try {
                setValuesOfFields(entry_id);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void initFriends() {
        ImageButton friends_button = (ImageButton) this.findViewById(R.id.btn_friends);
        final DBFriend dbc = new DBFriend(this);
        if (dbc.getAllFriends().size() == 0) {
            Helper.initCategories(this);
        }
        final List<Friend> all_friends = dbc.getAllFriends();

        for (Friend item : all_friends) {
            items.add(item.getName());
        }

        friends_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final List<Friend> all_friends = dbc.getAllFriends();

                for (Friend item : all_friends) {
                    items.add(item.getName());
                }

                String text = "Select Friends";

                final MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.multi_spinner);
                multiSpinner.setVisibility(v.VISIBLE);
                multiSpinner.setItems(items, text, new MultiSpinner.MultiSpinnerListener() {

                    @Override
                    public void onItemsSelected(boolean[] selected) {

                        for(int i = 0; i<items.size(); i++){
                            if(selected[i] == true){
                                friends.add(all_friends.get(i));
                            }
                        }
                        Helper.updateBadgeVisibility(badge_friends, true);
                    }
                });
            }
        });
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
                    Toast.makeText(getApplicationContext(), "Address: " + entry_address.getStreet() + "\nCountry: " + entry_address.getCountry(), Toast.LENGTH_SHORT).show();
                } else {
                    gps.showSettingsAlert();
                }
            }
        });
        location_button.callOnClick();
    }

    public void initDateButton() {
        Calendar currentDate = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
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

    public void initEditDateButton(int year, int month, int day) {
        Calendar currentDate = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                this, CreateEntryScreen.this, year, month, day);
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
            case WRITE_STORAGE_REQUEST:
            case CAMERA_REQUEST: {
                if (grant_results.length > 0
                        && grant_results[0] == PackageManager.PERMISSION_GRANTED && grant_results[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    ImageButton picture_button = (ImageButton) this.findViewById(R.id.btn_pictures);
                    picture_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ToDo: show feature disabled dialog
                        }
                    });
                    picture_button.performClick();
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
        maincategories = dbc.getMainCategories();
        strings_maincategories.add(empty_spinner_text);
        for (Category temp_cat : maincategories) {
            strings_maincategories.add(temp_cat.getName());
        }

        input_main_category = (Spinner) findViewById(R.id.input_category);
        ArrayAdapter<String> main_spinner = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, strings_maincategories);

        input_main_category.setAdapter(main_spinner);
        input_main_category.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        Category selected_category = Helper.getCategoryByName(maincategories, input_main_category.getSelectedItem().toString());
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

    public void checkPicturePermissions(CreateEntryScreen activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, WRITE_STORAGE_REQUEST);
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    public void checkGalleryPermissions(CreateEntryScreen activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    activity.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, WRITE_STORAGE_REQUEST);
                if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                        activity.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openGallery(activity);
                }
            } else {
                openGallery(activity);
            }
        } else {
            openGallery(activity);
        }
    }

    public void openGallery(final CreateEntryScreen activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, CAMERA_REQUEST);
    }

    public void loadingPopup(View v) {
        View view = findViewById(R.id.btn_pictures);
        PopupMenu menu = new PopupMenu(CreateEntryScreen.this, view);
        menu.inflate(R.menu.popup_entry_pictures);

        MenuPopupHelper menuHelper = new MenuPopupHelper(CreateEntryScreen.this, (MenuBuilder) menu.getMenu(), view);
        menuHelper.setForceShowIcon(true);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_camera:
                        checkPicturePermissions(CreateEntryScreen.this);
                        break;

                    case R.id.action_gallery:
                        checkGalleryPermissions(CreateEntryScreen.this);
                        break;

                    case R.id.action_remove:
                        removePictures();
                        break;

                    default:

                }
                return true;
            }
        });
        menuHelper.show();
    }

    public void removePictures() {
        Intent intent = new Intent(this, DeletePictureScreen.class);
        String[] file_paths = entry_picture_paths.toArray(new String[0]);
        intent.putExtra("file_paths", file_paths);
        startActivityForResult(intent, DELETE_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && data != null && data.getExtras() != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out_stream);
            Calendar calendar = Calendar.getInstance();
            String pictureName = new SimpleDateFormat("yyyy-MM-dd/HHmmssSSS").format(calendar.getTime()) + ".jpeg";
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, pictureName, null);
            Uri uri = Uri.parse(path);
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String absolute_path = cursor.getString(idx);
            entry_picture_paths.add(absolute_path);
            badge_camera.setText(Integer.toString(entry_picture_paths.size()));
        } else if (requestCode == PICK_IMAGE_REQUEST) {
            List<Uri> uris = new ArrayList<>();
            if (data.getData() != null) {
                Uri uri = data.getData();
                uris.add(uri);
            } else {
                if (data.getClipData() != null) {
                    ClipData clip_data = data.getClipData();
                    for (int i = 0; i < clip_data.getItemCount(); i++) {
                        ClipData.Item item = clip_data.getItemAt(i);
                        Uri uri = item.getUri();
                        uris.add(uri);
                    }
                }
            }
            for (Uri uri : uris) {
                String absolute_path = "";
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                String documentID = cursor.getString(0);
                documentID = documentID.substring(documentID.lastIndexOf(":") + 1);
                cursor.close();
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ?", new String[]{documentID}, null);
                cursor.moveToFirst();
                absolute_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                entry_picture_paths.add(absolute_path);
            }
            badge_camera.setText(Integer.toString(entry_picture_paths.size()));
        } else if (requestCode == DELETE_IMAGE_REQUEST && data != null) {
            String[] file_paths = data.getStringArrayExtra("file_paths");
            entry_picture_paths = new ArrayList<>(Arrays.asList(file_paths));
            badge_camera.setText(Integer.toString(entry_picture_paths.size()));
        } else {
            Toast.makeText(CreateEntryScreen.this, "No action performed!",
                    Toast.LENGTH_LONG).show();
        }
        Helper.updateBadgeVisibility(badge_camera, entry_picture_paths.size() > 0);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        entry_date = calendar.getTime();
        Helper.updateBadgeVisibility(badge_date, true);
    }

    public void saveEvent(View view) {
        input_event_title = (EditText) findViewById(R.id.input_title);
        String title = input_event_title.getText().toString();
        if (title.equals("")) {
            Toast.makeText(CreateEntryScreen.this, "Please define a Title!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        input_description = (EditText) findViewById(R.id.input_description);
        String description = input_description.getText().toString();
        input_main_category = (Spinner) findViewById(R.id.input_category);
        Category main_category = Helper.getCategoryByName(maincategories, input_main_category.getSelectedItem().toString());
        if (main_category == null) {
            Toast.makeText(CreateEntryScreen.this, "Please select a Main Category!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        input_sub_category = (Spinner) findViewById(R.id.input_subcategory);
        Category sub_category = Helper.getCategoryByName(subcategories, input_sub_category.getSelectedItem().toString());
        if (sub_category == null) {
            Toast.makeText(CreateEntryScreen.this, "Please select a Subcategory!",
                    Toast.LENGTH_LONG).show();
            return;
        }
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
        if (entry_picture_paths.size() > 0) {
            DBPicture dbp = new DBPicture(this);
            for (String file_path : entry_picture_paths) {
                Picture picture = dbp.getPictureByPath(file_path);
                if (picture == null) {
                    picture = new Picture();
                    picture.setFilePath(file_path);
                    String[] path_parts = file_path.split("/");
                    picture.setName(path_parts[path_parts.length - 1]);
                    long id = dbp.insert(picture);
                    picture.setId((int) id);
                }
                entry.addPicture(picture);
            }
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
            entry.setAddressId((int) id);
        }
        if (friends.size() != 0) {
            entry.setFriends(friends);
        }
        if (entry_id != 0) {
            DBEntry dbEntry = new DBEntry(this);
            Address address = dbEntry.getEntry(entry_id).getAddress();
            int addressId = dbEntry.getEntry(entry_id).getAddressId();
            //if (entry_address != null && (entry.getAddress() == null || (entry.getAddress() != null && !(entry_address.getStreet().equals(entry.getAddress().getStreet()))))) {
            if(entry_address != null && (!(entry_address.getStreet().equals(address)))) {
                DBAddress dbAddress = new DBAddress(this);
                addressId = (int) dbAddress.insert(entry_address);
            }
            entry.setId(entry_id);
            entry.setAddressId(addressId);
            updateEntry(entry);
        } else {
            insertEntryToDatabase(entry);
        }
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
    }

    public void updateEntry(Entry entry) {
        DBEntry db_entry = new DBEntry(this);
        db_entry.updateEntry(entry);
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

    public void setValuesOfFields(int entry_id) throws ParseException {

        Entry entry = Helper.getEntryComplete(this, entry_id);
        fillSubCategories(entry.getMainCategoryId());

        input_event_title = (EditText) findViewById(R.id.input_title);
        input_event_title.setText(entry.getTitle());

        input_description = (EditText) findViewById(R.id.input_description);
        input_description.setText(entry.getDescription());

        input_main_category = (Spinner) findViewById(R.id.input_category);
        int main_category_index = strings_maincategories.indexOf(entry.getMainCategory().getName());
        input_main_category.setSelection(main_category_index);


        input_sub_category = (Spinner) findViewById(R.id.input_subcategory);
        final int sub_category_index = strings_subcategories.indexOf(entry.getSubCategory().getName());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                input_sub_category.setSelection(sub_category_index);
            }
        }, 100);
        if (entry.getAddress() != null) {
            entry_address = entry.getAddress();
            Helper.updateBadgeVisibility(badge_address, true);
        }
        entry_date = entry.getDate();
        Date date = entry_date;
        String[] date_array = entry_date.toString().split(" ");
        Helper.updateBadgeVisibility(badge_date, true);
        Date moth = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(date_array[1]);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        initEditDateButton(Integer.parseInt(date_array[5]), month, Integer.parseInt(date_array[2]));
        //Todo: Set pitures by Sandy Nocker
    }
}