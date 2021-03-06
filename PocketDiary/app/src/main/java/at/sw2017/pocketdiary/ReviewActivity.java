package at.sw2017.pocketdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBEntry;

/**
 * Created by marku on 04.05.2017.
 */

public class ReviewActivity extends Activity{

    List<Entry> entry_list = new ArrayList<>();
    ArrayList<Integer> db_ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        if(getIntent().getExtras() != null){
            List<String> entries_ids_stat = getIntent().getStringArrayListExtra("entries_ids");
            for(String id : entries_ids_stat) {
                DBEntry dbe = new DBEntry(this);
                int id_int = Integer.parseInt(id);
                entry_list.add(dbe.getEntry(id_int));
            }
        }
        else entry_list = getData();
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
}
