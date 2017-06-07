package at.sw2017.pocketdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.CustomDate;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Statistic;
import at.sw2017.pocketdiary.business_objects.Toast;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBPicture;
import at.sw2017.pocketdiary.database_access.DBFriend;
import at.sw2017.pocketdiary.database_access.DBStatistic;
import at.sw2017.pocketdiary.database_access.DBStatisticAnalysis;


/**
 * Created by marku on 30.05.2017.
 */

public class StatisticAnalysisActivity extends Activity {

    private int statistic_id;
    private CharSequence[] final_sub_cats = {};
    ArrayList<Integer> db_ids = new ArrayList<>();
    ArrayAdapter adapter;
    Set<Entry> set_entry;
    ArrayList<String> all_entries_ids_str = new ArrayList<>();
    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_statistic);
        Intent intent = getIntent();
        DBStatistic dbs = new DBStatistic(this);
        statistic_id = Integer.parseInt(intent.getStringExtra("statistic_id"));
        Statistic statistic = dbs.getStatistic(statistic_id);
        List<Entry> entries_main = new ArrayList<>();
        List<Entry> entries_sub = new ArrayList<>();
        List<Entry> entries_term = new ArrayList<>();
        List<Entry> entries_dates = new ArrayList<>();
        if(statistic.getCategoryId() != null)
            entries_main = getByMainCategory(statistic.getCategoryId());
        if(statistic.getSubCategoryId() != null)
            entries_sub = getBySubCategory(statistic.getSubCategoryId());
        if(statistic.getSearchTerm() != null)
            entries_term = getBySearchTerm(statistic.getSearchTerm());

        if(statistic.getDateUntil() != null && statistic.getDateFrom() == null) {
            Calendar calendar = Calendar.getInstance();
            CustomDate cs_date = new CustomDate();
            calendar.set(1970, 1, 1, 0, 0, 0);
            Date from_date = calendar.getTime();
            entries_dates = getByDates(from_date, statistic.getDateUntil());
        }
        if(statistic.getDateUntil() == null && statistic.getDateFrom() != null) {
            Calendar calendar = Calendar.getInstance();
            CustomDate cs_date = new CustomDate();
            calendar.set(cs_date.getCurrentYear(), cs_date.getCurrentMonth(), cs_date.getCurrentDay(), 0, 0, 0);
            Date end_date = calendar.getTime();
            entries_dates = getByDates(statistic.getDateFrom(), end_date);
        }
        if(statistic.getDateUntil() != null && statistic.getDateFrom() != null) {
            entries_dates = getByDates(statistic.getDateFrom(), statistic.getDateUntil());
        }
        set_entry = intersectLists(entries_main, entries_sub, entries_term, entries_dates);
        fillListView(set_entry);
        setClickListener();
    }

    private void setClickListener(){
        ImageButton img_btn_dates = (ImageButton) findViewById(R.id.stat_analysis_btn_dates);
        ImageButton img_btn_cal = (ImageButton) findViewById(R.id.stat_analysis_btn_calendar);
        ImageButton img_btn_loc = (ImageButton) findViewById(R.id.stat_analysis_btn_location);
        ImageButton img_btn_camera = (ImageButton) findViewById(R.id.stat_analysis_btn_camera);
        ImageButton img_btn_friends = (ImageButton) findViewById(R.id.stat_analysis_btn_friends);
        Button btn_del = (Button) findViewById(R.id.stat_analysis_delete_stat);
        Button btn_categories = (Button) findViewById(R.id.stat_analysis_categories);
        img_btn_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillListViewImgBtnCal();
            }
        });
        img_btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillListViewImgBtnLoc();
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogueDeleteStatistic();

            }
        });
        btn_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillListViewCat();
            }
        });
        img_btn_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StatisticAnalysisActivity.this, ReviewActivity.class);
                i.putStringArrayListExtra("entries_ids", all_entries_ids_str);
                startActivity(i);
            }
        });
        img_btn_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillListViewFriends();
            }
        });
        img_btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillGridViewPictures();
            }
        });
    }

    private void fillListViewFriends(){
        final ListView review_listview = (ListView) findViewById(R.id.stat_analysis_lv);
        Map<String, Integer> map_friends = new HashMap<String, Integer>();
        db_ids = new ArrayList<>();
        for(Entry entry : set_entry){
            String friends_ids = entry.getAllFriends();
            if(friends_ids == null) continue;
            String[] friends_ids_list = friends_ids.split(",");
            for(String id : friends_ids_list){
                DBFriend dbf = new DBFriend(this);
                Friend friend = dbf.getFriend(Integer.parseInt(id));
                if(map_friends.containsKey(friend.getName())){
                    map_friends.put(friend.getName(), map_friends.get(friend.getName())+1);

                }
                else {
                    map_friends.put(friend.getName(), 1);
                    db_ids.add(friend.getId());
                }


            }
        }

        ArrayList<String> final_friends = new ArrayList<>();
        for(String key : map_friends.keySet()){
            Integer value = map_friends.get(key);
            final_friends.add(String.valueOf(value) + " x " + key);
        }
        GridView gridview = (GridView) findViewById(R.id.stat_analysis_pictures);
        gridview.setVisibility(View.INVISIBLE);
        adapter = new ArrayAdapter<>(this, R.layout.layout_listview_item, final_friends);
        review_listview.setAdapter(adapter);
        review_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String friend_name = review_listview.getItemAtPosition(position).toString().split("x")[1].trim();
                List<Entry> entries_list = new ArrayList<Entry>();
                for(Entry entry : set_entry){
                    String friends_ids = entry.getAllFriends();
                    if(friends_ids == null) continue;
                    String[] friends_ids_list = friends_ids.split(",");
                    for(String id_friend : friends_ids_list){
                        DBFriend dbf = new DBFriend(StatisticAnalysisActivity.this);
                        Friend friend = dbf.getFriend(Integer.parseInt(id_friend));
                        if(friend_name.equals(friend.getName())){
                            entries_list.add(entry);
                        }
                    }
                }
                createAlertDialogueWithEntries(entries_list);
            }
        });


    }

    private void fillGridViewPictures() {

        final ArrayList<String> paths = new ArrayList<>();
        ArrayList<String> fileNames = new ArrayList<>();
        db_ids = new ArrayList<>();
        for (Entry entry : set_entry){
            DBPicture dbp = new DBPicture(this);
            if(entry.getPictures() != null) {
                Picture picture = dbp.getPicture(entry.getId());
                if (picture != null) {
                    if (picture.getFilePath() != null) {
                        paths.add(picture.getFilePath());
                        if (picture.getName() != null) {
                            fileNames.add(picture.getName());
                        }
                        db_ids.add(entry.getId());
                    }
                }
            }
        }

        ListView review_listview = (ListView) findViewById(R.id.stat_analysis_lv);
        adapter = new ArrayAdapter<>(this, R.layout.layout_listview_item);
        review_listview.setAdapter(adapter);
        GridView gridview = (GridView) findViewById(R.id.stat_analysis_pictures);
        gridview.setVisibility(View.VISIBLE);
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
            imageItems.add(new ImageItem(bitmap, fileNames.get(i)));
        }

        ImageAdapter imageAdapter = new ImageAdapter(this, R.layout.layout_gridview, imageItems);
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent i = new Intent(StatisticAnalysisActivity.this, ShowPictureScreen.class);
                i.putExtra("entry_id", paths.get(position));
                startActivity(i);
            }
        });

    }

    private void fillListViewCat(){
        final ListView review_listview = (ListView) findViewById(R.id.stat_analysis_lv);
        Map<String, Integer> map_main = new HashMap<String, Integer>();
        db_ids = new ArrayList<>();
        for (Entry entry : set_entry){
            DBCategory dbc = new DBCategory(this);
            String category_name = dbc.getMainCategory(entry.getMainCategoryId()).getName();
            if(map_main.containsKey(category_name)){
                map_main.put(category_name, map_main.get(category_name)+1);
            }
            else map_main.put(category_name, 1);
            db_ids.add(entry.getId());

        }

        final Map<String, Integer> map_sub = new HashMap<String, Integer>();
        for (Entry entry : set_entry){
            DBCategory dbc = new DBCategory(this);
            String category_name = dbc.getMainCategory(entry.getSubCategoryId()).getName();
            if(map_sub.containsKey(category_name)){
                map_sub.put(category_name, map_sub.get(category_name)+1);
            }
            else map_sub.put(category_name, 1);
            db_ids.add(entry.getId());

        }

        ArrayList<String> final_cats = new ArrayList<>();
        for(String key : map_main.keySet()){
            Integer value = map_main.get(key);
            final_cats.add(String.valueOf(value) + " x " + key);
        }

        GridView gridview = (GridView) findViewById(R.id.stat_analysis_pictures);
        gridview.setVisibility(View.INVISIBLE);
        adapter = new ArrayAdapter<>(this, R.layout.layout_listview_item, final_cats);
        review_listview.setAdapter(adapter);
        review_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String main_cat_name = review_listview.getItemAtPosition(position).toString().split("x")[1].trim();
                DBCategory dbc = new DBCategory(StatisticAnalysisActivity.this);
                Category main_cat = dbc.getMainCategoryByName(main_cat_name);
                List<Category> sub_categories = dbc.getSubCategories(main_cat.getId());
                ArrayList<String> final_sub_cats = new ArrayList<>();
                for(Category cat : sub_categories){
                    if(map_sub.containsKey(cat.getName())){
                        final_sub_cats.add(String.valueOf(map_sub.get(cat.getName())) + " x " + cat.getName());
                    }
                }
                final CharSequence[] subs = final_sub_cats.toArray(new CharSequence[final_sub_cats.size()]);
                showSubCategories(subs);
            }
        });
    }


    private void showSubCategories(final CharSequence[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticAnalysisActivity.this);
        builder.setTitle("Subcategories")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String subcat = items[which].toString().split("x")[1].trim();
                        List<Entry> entry_list = new ArrayList<Entry>();
                        for(Entry entry : set_entry){
                            DBCategory dbc = new DBCategory(StatisticAnalysisActivity.this);
                            Category category = dbc.getCategory(entry.getSubCategoryId());
                            if(category.getName().equals(subcat)){
                                entry_list.add(entry);
                            }
                        }
                        createAlertDialogueWithEntries(entry_list);
                    }
                });
        builder.create();
        builder.show();
    }

    private void createAlertDialogueDeleteStatistic(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(StatisticAnalysisActivity.this);
        builder1.setMessage("Do you want to delete this statistic?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DBStatistic dbs = new DBStatistic(StatisticAnalysisActivity.this);
                        dbs.delete(statistic_id);
                        Intent intent = new Intent(StatisticAnalysisActivity.this, StatisticScreenActivity.class);
                        startActivity(intent);
                        Toast toast = new Toast("Statistic deleted successfully.", StatisticAnalysisActivity.this);
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void createAlertDialogueWithEntries(final List<Entry> entries){
        List<String> entries_name = new ArrayList<>();
        for(Entry entry : entries){
            entries_name.add(entry.getTitle());
        }
        final CharSequence[] entry_names = entries_name.toArray(new CharSequence[entries_name.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticAnalysisActivity.this);
        builder.setTitle("Entries")
                .setItems(entry_names, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Entry entry = entries.get(which);
                        Intent i = new Intent(StatisticAnalysisActivity.this, ShowEntryScreen.class);
                        i.putExtra("entry_id", String.valueOf(entry.getId()));
                        startActivity(i);
                    }
                });
        builder.create();
        builder.show();
    }

    private void fillListViewImgBtnLoc() {
        ListView review_listview = (ListView) findViewById(R.id.stat_analysis_lv);
        ArrayList<String> addresses = new ArrayList<>();
        db_ids = new ArrayList<>();
        for (Entry entry : set_entry){
            DBAddress dba = new DBAddress(this);
            if(entry.getAddressId() != null) {
                Address address = dba.getAddress(entry.getAddressId());
                if (address != null) {
                    if (address.getPoi() != null) {
                        addresses.add(address.getPoi());
                    } else addresses.add(address.getStreet());
                    db_ids.add(entry.getId());
                }
            }
        }

        GridView gridview = (GridView) findViewById(R.id.stat_analysis_pictures);
        gridview.setVisibility(View.INVISIBLE);
        adapter = new ArrayAdapter<>(this, R.layout.layout_listview_item, addresses);
        review_listview.setAdapter(adapter);
        review_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(StatisticAnalysisActivity.this, ShowEntryScreen.class);
                i.putExtra("entry_id", Integer.toString(db_ids.get(position)));
                startActivity(i);
            }
        });
    }

    private void fillListViewImgBtnCal() {
        SimpleDateFormat date_fromat = new SimpleDateFormat("dd.MM.yyyy");
        ListView review_listview = (ListView) findViewById(R.id.stat_analysis_lv);
        ArrayList<String> entries_dates = new ArrayList<>();
        db_ids = new ArrayList<>();
        for (Entry entry : set_entry){
            entries_dates.add(date_fromat.format(entry.getDate()));
            db_ids.add(entry.getId());
        }

        GridView gridview = (GridView) findViewById(R.id.stat_analysis_pictures);
        gridview.setVisibility(View.INVISIBLE);
        adapter = new ArrayAdapter<>(this, R.layout.layout_listview_item, entries_dates);
        review_listview.setAdapter(adapter);
        review_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(StatisticAnalysisActivity.this, ShowEntryScreen.class);
                i.putExtra("entry_id", Integer.toString(db_ids.get(position)));
                startActivity(i);
            }
        });
    }

    private Set<Entry> intersectLists(List<Entry> entries_main, List<Entry> entries_sub, List<Entry> entries_term, List<Entry> entries_dates) {
        List<Integer> entries_main_ids = new ArrayList<>();
        List<Integer> entries_sub_ids = new ArrayList<>();
        List<Integer> entries_term_ids = new ArrayList<>();
        List<Integer> entries_dates_ids = new ArrayList<>();
        Set<Integer> all_entries_ids = new HashSet<>();
        for(Entry entry : entries_main){
            entries_main_ids.add(entry.getId());
            all_entries_ids.add(entry.getId());
        }
        for(Entry entry : entries_sub){
            entries_sub_ids.add(entry.getId());
            all_entries_ids.add(entry.getId());
        }
        for(Entry entry : entries_term){
            entries_term_ids.add(entry.getId());
            all_entries_ids.add(entry.getId());
        }
        for(Entry entry : entries_dates){
            entries_dates_ids.add(entry.getId());
            all_entries_ids.add(entry.getId());
        }

        if(entries_main_ids.size() > 0) all_entries_ids.retainAll(entries_main_ids);
        if(entries_sub_ids.size() > 0) all_entries_ids.retainAll(entries_sub_ids);
        if(entries_term_ids.size() > 0) all_entries_ids.retainAll(entries_term_ids);
        if(entries_dates_ids.size() > 0) all_entries_ids.retainAll(entries_dates_ids);

        Set<Entry> set_entries = new HashSet<>();
        for(Integer id : all_entries_ids){
            DBEntry dbe = new DBEntry(this);
            set_entries.add(dbe.getEntry(id));
            all_entries_ids_str.add(id.toString());
        }

        return set_entries;
    }

    public List<Entry> getByMainCategory(int id){
        DBStatisticAnalysis dbsa = new DBStatisticAnalysis(this);
        List<Entry> entries = dbsa.getEntriesByMainCategoryId(id);
        if(entries.size() > 0) Log.d("Verbose", entries.get(0).getTitle());
        return entries;
    }

    public List<Entry> getBySubCategory(int id){
        DBStatisticAnalysis dbsa = new DBStatisticAnalysis(this);
        List<Entry> entries = dbsa.getEntriesBySubCategoryId(id);
        if(entries.size() > 0) Log.d("Verbose", "SUB:" + entries.get(0).getDescription());
        return entries;
    }

    public List<Entry> getBySearchTerm(String search_term){
        DBStatisticAnalysis dbsa = new DBStatisticAnalysis(this);
        List<Entry> entries = dbsa.getEntriesBySearchTerm(search_term);
        if(entries.size() > 0) {
            for(Entry entry : entries){
                Log.d("Verbose", "SEARCH:" + entry.getTitle());
            }

        }
        return entries;
    }

    public List<Entry> getByDates(Date date_start, Date date_end){
        DBStatisticAnalysis dbsa = new DBStatisticAnalysis(this);
        List<Entry> entries = dbsa.getEntriesBetweenDates(date_start, date_end);
        if(entries.size() > 0) {
            for(Entry entry : entries){
                Log.d("Verbose", "DATE:" + entry.getTitle());
            }

        }
        return entries;
    }


    public void fillListView(Set<Entry> entries) {
        ListView review_listview = (ListView) findViewById(R.id.stat_analysis_lv);
        ArrayList<String> entries_names = new ArrayList<>();
        db_ids = new ArrayList<>();
        for (Entry entry : entries){
            entries_names.add(entry.getTitle());
            db_ids.add(entry.getId());
        }

        GridView gridview = (GridView) findViewById(R.id.stat_analysis_pictures);
        gridview.setVisibility(View.INVISIBLE);
        adapter = new ArrayAdapter<>(this, R.layout.layout_listview_item, entries_names);
        review_listview.setAdapter(adapter);
        review_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(StatisticAnalysisActivity.this, ShowEntryScreen.class);
                i.putExtra("entry_id", Integer.toString(db_ids.get(position)));
                startActivity(i);
            }
        });
    }

}
