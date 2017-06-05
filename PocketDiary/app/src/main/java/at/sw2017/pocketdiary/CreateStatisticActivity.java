package at.sw2017.pocketdiary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.CustomDate;
import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Statistic;
import at.sw2017.pocketdiary.business_objects.Toast;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBFriend;
import at.sw2017.pocketdiary.database_access.DBStatistic;

/**
 * Created by marku on 25.05.2017.
 */

public class CreateStatisticActivity extends Activity{

    private Spinner spinner_main_cat;
    private Spinner spinner_sub_cat;
    private Spinner spinner_friends;
    private String empty_spinner_text = "Select Category";
    private String empty_spinner_txt_friends = "Select Friend";
    private int crt_parent_id = -1;
    List<String> strings_maincategories = new ArrayList<>();
    List<Category> maincategories = new ArrayList<>();
    boolean first_touch = true;
    private Date date_until_date = null;
    private Date date_from_date = null;

    List<Category> subcategories = new ArrayList<>();
    List<String> strings_subcategories = new ArrayList<>();

    List<Friend> friends_list = new ArrayList<>();
    List<String> strings_friends_list = new ArrayList<>();

    private Category main_cat_obj = null;
    private Category sub_cat_obj = null;
    private String friends_txt = null;

    private String date_from_format = null;
    private String date_til_format = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_statistic);
        //insertTestData();
        setDatePicker();
        setMainCategorySpinner();
        setFriendsSpinner();
        setButtons();
    }

    private void setDatePicker(){
        final EditText date_from = (EditText) findViewById(R.id.create_stats_inp_from);
        final CustomDate date = new CustomDate();
        date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(CreateStatisticActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        date_from.setText(dayOfMonth + "." + month + "." + year);
                        date_from_format = year + "-" + month + "-" + dayOfMonth;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month-1, dayOfMonth, 0, 0, 0);
                        date_from_date = calendar.getTime();
                    }
                }, date.getCurrentYear(), date.getCurrentMonth(), date.getCurrentDay());
                dialog.show();
            }
        });

        final EditText date_til = (EditText) findViewById(R.id.create_stats_inp_til);
        date_til.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(CreateStatisticActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        date_til.setText(dayOfMonth + "." + month + "." + year);
                        date_til_format = year + "-" + month + "-" + dayOfMonth;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month-1, dayOfMonth, 0, 0, 0);
                        date_until_date = calendar.getTime();
                    }
                }, date.getCurrentYear(), date.getCurrentMonth(), date.getCurrentDay());
                dialog.show();
            }
        });
    }

    private void setMainCategorySpinner(){
        DBCategory dbc = new DBCategory(this);
        if (dbc.getAllCategories().size() == 0) {
            Helper.initCategories(this);
        }
        maincategories = dbc.getMainCategories();
        strings_maincategories.add(empty_spinner_text);
        for (Category temp_cat : maincategories) {
            strings_maincategories.add(temp_cat.getName());
        }

        spinner_main_cat = (Spinner) findViewById(R.id.create_stats_spin_main_cat);
        final ArrayAdapter<String> main_spinner = new ArrayAdapter<String>(
                this, R.layout.layout_spinner_item, strings_maincategories);

        spinner_main_cat.setAdapter(main_spinner);
        spinner_main_cat.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        Category selected_category = Helper.getCategoryByName(maincategories, spinner_main_cat.getSelectedItem().toString());
                        if (selected_category != null) {
                            crt_parent_id = selected_category.getId();
                            first_touch = false;
                            subcategories = new ArrayList<>();
                            strings_subcategories = new ArrayList<>();
                            main_cat_obj = selected_category;
                            setSubCategorySpinner(crt_parent_id);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
    }

    private void setSubCategorySpinner(int parent_id){
        DBCategory dbc = new DBCategory(this);
        subcategories = dbc.getSubCategories(parent_id);
        if (subcategories.size() == 0) {
            Toast toast = new Toast("Kane subs", this);
        }
        strings_subcategories.add(empty_spinner_text);
        for (Category temp_cat : subcategories) {
            strings_subcategories.add(temp_cat.getName());
        }

        spinner_sub_cat = (Spinner) findViewById(R.id.create_stats_spin_sub_cat);
        final ArrayAdapter<String> sub_spinner = new ArrayAdapter<String>(
                this, R.layout.layout_spinner_item, strings_subcategories);

        spinner_sub_cat.setAdapter(sub_spinner);
        spinner_sub_cat.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        Category selected_category = Helper.getCategoryByName(subcategories, spinner_sub_cat.getSelectedItem().toString());
                        if (selected_category != null) {
                            crt_parent_id = selected_category.getId();
                            sub_cat_obj = selected_category;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );

    }

    private void setFriendsSpinner(){
        DBFriend dbf = new DBFriend(this);
        friends_list = dbf.getAllFriends();
        if(friends_list.size() == 0){
            Toast toast = new Toast("Kane friends", this);
        }
        strings_friends_list.add(empty_spinner_txt_friends);
        for(Friend friend : friends_list){
            strings_friends_list.add(friend.getName());
        }
        spinner_friends = (Spinner) findViewById(R.id.create_stats_spin_friends);
        final ArrayAdapter<String> friend_spinner = new ArrayAdapter<String>(
                this, R.layout.layout_spinner_item, strings_friends_list);

        spinner_friends.setAdapter(friend_spinner);
        spinner_friends.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        /*Category selected_category = Helper.getCategoryByName(subcategories, spinner_sub_cat.getSelectedItem().toString());
                        if (selected_category != null) {
                            crt_parent_id = selected_category.getId();
                        }*/
                        friends_txt = spinner_friends.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );

    }

    private void setButtons(){
        Button save = (Button) findViewById(R.id.create_stats_btn_save);
        Button cancel = (Button) findViewById(R.id.create_stats_btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateStatisticActivity.this, StatisticScreenActivity.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfInputValid()){
                    insertStatistic();
                }
            }
        });
    }

    private boolean checkIfInputValid(){
        boolean data_is_valid = true;
        Toast toast = new Toast();
        int value_counter = 1;
        EditText title = (EditText) findViewById(R.id.create_stats_inp_title);
        EditText date_from = (EditText) findViewById(R.id.create_stats_inp_from);
        EditText date_til = (EditText) findViewById(R.id.create_stats_inp_til);
        EditText term = (EditText) findViewById(R.id.create_stats_inp_term);
        Spinner main_cat = (Spinner) findViewById(R.id.create_stats_spin_main_cat);
        Spinner sub_cat = (Spinner) findViewById(R.id.create_stats_spin_sub_cat);
        Spinner friends = (Spinner) findViewById(R.id.create_stats_spin_friends);

        if(title.getText().toString().trim().length() == 0){
            data_is_valid = false;
            toast.printToast("Provide a valid title.", this);
            return data_is_valid;
        }
        if(date_from.getText().toString().trim().length() != 0){
            value_counter++;
        }
        if(date_til.getText().toString().trim().length() != 0){
            value_counter++;
        }
        if(!main_cat.getSelectedItem().toString().equals(empty_spinner_text)){
            value_counter++;
        }
        if(sub_cat.getSelectedItem() != null &&
                !sub_cat.getSelectedItem().toString().equals(empty_spinner_text)){
            value_counter++;
        }
        if(!friends.getSelectedItem().toString().equals(empty_spinner_txt_friends)){
            value_counter++;
        }
        if(term.getText().toString().trim().length() != 0){
            value_counter++;
        }
        if(value_counter < 2){
            data_is_valid = false;
            toast.printToast("Please provide at least on argument additional to the title.", this);
        }

        return data_is_valid;
    }

    private void insertStatistic(){
        Statistic statistic = new Statistic();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        EditText title = (EditText) findViewById(R.id.create_stats_inp_title);
        EditText date_from = (EditText) findViewById(R.id.create_stats_inp_from);
        EditText date_til = (EditText) findViewById(R.id.create_stats_inp_til);
        EditText term = (EditText) findViewById(R.id.create_stats_inp_term);
        Spinner main_cat = (Spinner) findViewById(R.id.create_stats_spin_main_cat);
        Spinner sub_cat = (Spinner) findViewById(R.id.create_stats_spin_sub_cat);
        Spinner friends = (Spinner) findViewById(R.id.create_stats_spin_friends);

        statistic.setTitle(title.getText().toString());

        if(date_from.getText().toString().trim().length() != 0){
            statistic.setDateFrom(date_from_date);
        }
        if(date_til.getText().toString().trim().length() != 0){
            statistic.setDateUntil(date_until_date);
        }
        if(!main_cat.getSelectedItem().toString().equals(empty_spinner_text)){
            statistic.setCategoryId(Helper.getCategoryByName(maincategories, main_cat.getSelectedItem().toString()).getId());
        }
        if(sub_cat.getSelectedItem() != null &&
                !sub_cat.getSelectedItem().toString().equals(empty_spinner_text)){
            statistic.setSubCategoryId(Helper.getCategoryByName(subcategories, sub_cat.getSelectedItem().toString()).getId());
        }
        if(!friends.getSelectedItem().toString().equals(empty_spinner_txt_friends)){

        }
        if(term.getText().toString().trim().length() != 0){
            statistic.setSearchTerm(term.getText().toString());
        }

        DBStatistic db_insert = new DBStatistic(this);
        db_insert.insert(statistic);
        Intent intent = new Intent(CreateStatisticActivity.this, StatisticScreenActivity.class);
        startActivity(intent);
        Toast toast = new Toast("Statistic created successfully.", this);
    }

    private void insertTestData(){
        DBFriend db_insert = new DBFriend(this);

        Friend friend_1 = new Friend("Hofer Junior");
        db_insert.insert(friend_1);

        Friend friend_2 = new Friend("Hermann");
        db_insert.insert(friend_2);

        Friend friend_3 = new Friend("Minze");
        db_insert.insert(friend_3);
    }
}
