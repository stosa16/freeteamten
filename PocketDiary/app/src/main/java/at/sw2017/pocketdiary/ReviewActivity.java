package at.sw2017.pocketdiary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBEntry;

/**
 * Created by marku on 04.05.2017.
 */

public class ReviewActivity extends Activity{

    List<Entry> entry_list;
    ArrayList<Integer> db_ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        //insertTestData();
        entry_list = getData();
        CalendarDay[] dates_list = getAllDates(entry_list);
        String current_date = getCurrentDate();
        setCalendar(dates_list, getCurrentDay(current_date), getCurrentMonth(current_date), getCurrentYear(current_date));
        fillListView(getCurrentDay(current_date), getCurrentMonth(current_date)-1, getCurrentYear(current_date));
    }

    private List<Entry> getData(){
        DBEntry db_entry = new DBEntry(this);
        List<Entry> entry_list = db_entry.getAllEntries();
        return entry_list;
    }

    private CalendarDay[] getAllDates(List<Entry> entry_list){
        CalendarDay[] list_dates = new CalendarDay[entry_list.size()];
        int i = 0;
        for(Entry entry : entry_list){
            Date date = entry.getDate();
            Calendar lCal = Calendar.getInstance();
            lCal.setTime(date);
            int year = lCal.get(Calendar.YEAR);
            int month = lCal.get(Calendar.MONTH);
            int day = lCal.get(Calendar.DATE);
            list_dates[i] = new CalendarDay(year, month, day);
            i++;
        }
        return list_dates;
    }

    private void insertTestData(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 5, 14, 0, 0, 0);
        Date entry_date = calendar.getTime();

        Entry db_test_entry = new Entry();
        db_test_entry.setTitle("Event 7");
        db_test_entry.setDescription("This is a description");
        db_test_entry.setMainCategoryId(1);
        db_test_entry.setSubCategoryId(1);
        db_test_entry.setDate(entry_date);
        DBEntry db_insert = new DBEntry(this);
        db_insert.insert(db_test_entry);

        Entry db_test_entry_2 = new Entry();
        db_test_entry_2.setTitle("Event 2");
        db_test_entry_2.setDescription("This is a description 2");
        db_test_entry_2.setMainCategoryId(1);
        db_test_entry_2.setSubCategoryId(1);
        db_test_entry_2.setDate(entry_date);
        db_insert.insert(db_test_entry_2);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2017, 3, 17, 0, 0, 0);
        Date entry_date2 = calendar2.getTime();

        Entry db_test_entry_3 = new Entry();
        db_test_entry_3.setTitle("Event 8");
        db_test_entry_3.setDescription("This is a description 8");
        db_test_entry_3.setMainCategoryId(1);
        db_test_entry_3.setSubCategoryId(1);
        db_test_entry_3.setDate(entry_date2);
        db_insert.insert(db_test_entry_3);

        Entry db_test_entry_4 = new Entry();
        db_test_entry_4.setTitle("Event Today");
        db_test_entry_4.setDescription("This is a description 8");
        db_test_entry_4.setMainCategoryId(1);
        db_test_entry_4.setSubCategoryId(1);
        db_test_entry_4.setDate((Date) Calendar.getInstance().getTime());
        db_insert.insert(db_test_entry_4);
    }

    public void setCalendar(CalendarDay[] dates_list, int day, int month, int year){
        MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.review_cal_view);
        Collection<CalendarDay> dates = new ArrayList<>(Arrays.asList(dates_list));
        int color = Color.parseColor("#BF0023");
        EventDecorator dec = new EventDecorator(color, dates);
        calendar.addDecorator(dec);
        CalendarDay crt_day = new CalendarDay(year,month-1,day);
        calendar.setDateSelected(crt_day, true);
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int day = date.getDay();
                int month = date.getMonth();
                int year = date.getYear();
                db_ids = new ArrayList<>();
                fillListView(day, month, year);
            }
        });
    }

    private void fillListView(int day, int month, int year){
        ListView review_listview = (ListView) findViewById(R.id.review_list_view);
        ArrayList<String> events = new ArrayList<>();
        for (Entry entry : entry_list){
            Date date = entry.getDate();
            Calendar lCal = Calendar.getInstance();
            lCal.setTime(date);
            int year_date = lCal.get(Calendar.YEAR);
            int month_date = lCal.get(Calendar.MONTH);
            int day_date = lCal.get(Calendar.DATE);
            if(year_date == year && month_date == month && day_date == day){
                events.add(entry.getTitle());
                db_ids.add(entry.getId());
            }
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, events);
        review_listview.setAdapter(adapter);
        review_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ReviewActivity.this, ShowEntryScreen.class);
                i.putExtra("entry_id", Integer.toString(db_ids.get(position)));
                startActivity(i);
            }
        });
    }

    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private int getCurrentDay(String date){
        return Integer.parseInt(date.split("-")[2]);
    }

    private int getCurrentMonth(String date){
        return Integer.parseInt(date.split("-")[1]);
    }

    private int getCurrentYear(String date){
        return Integer.parseInt(date.split("-")[0]);
    }

    public void printToast(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
