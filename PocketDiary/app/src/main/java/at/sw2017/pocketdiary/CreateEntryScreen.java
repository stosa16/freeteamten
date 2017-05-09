package at.sw2017.pocketdiary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Entry;
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        entry_date = calendar.getTime();
        Helper.updateBadgeVisibility(badge_date, true);
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
        long id = insertEntryToDatabase(entry);
        //Intent intent = new Intent(this, StartScreen.class);
        Intent intent = new Intent(this, StartScreen.class);
        intent.putExtra("entry_id", Integer.toString((int) id));
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