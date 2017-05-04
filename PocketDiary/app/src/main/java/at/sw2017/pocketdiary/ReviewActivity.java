package at.sw2017.pocketdiary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

/**
 * Created by marku on 04.05.2017.
 */

public class ReviewActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        setCalendar();
        String current_date = getCurrentDate();
        fillListView(getCurrentDay(current_date), getCurrentMonth(current_date), getCurrentYear(current_date));

    }

    private void setCalendar(){
        MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.review_cal_view);
        CalendarDay[] initList = {new CalendarDay(2017,4,1), new CalendarDay(2017,4,2), new CalendarDay(2017,4,17)};
        Collection<CalendarDay> dates = new ArrayList<>(Arrays.asList(initList));
        int color = Color.parseColor("#bf0023");
        EventDecorator dec = new EventDecorator(color, dates);
        calendar.addDecorator(dec);
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int day = date.getDay();
                int month = date.getMonth() + 1;
                int year = date.getYear();
                fillListView(day, month, year);
            }
        });
    }

    private void fillListView(int day, int month, int year){
        ListView review_listview = (ListView) findViewById(R.id.review_list_view);
        String[] events;
        switch (day){
            case 1:
                events = new String[] {"Tag 1 1", "Tag 1 2", "Tag 1 3", "Tag 1 4", "Tag 1 5", "Tag 1 6"};
                break;
            case 2:
                events = new String[] {"Tag 2 1", "Tag 2 2", "Tag 2 3", "Tag 2 4", "Tag 2 5", "Tag 2 6"};
                break;
            default:
                events = new String[] {"Def 2 1", "Def 2 2", "Def 2 3", "Def 2 4", "Def 2 5", "Def 2 6"};
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);
        review_listview.setAdapter(adapter);
        review_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                printToast("Clicked");

            }
        });
    }

    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.M.yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private int getCurrentDay(String date){
        return Integer.parseInt(date.split("\\.")[0]);
    }

    private int getCurrentMonth(String date){
        return Integer.parseInt(date.split("\\.")[1]);
    }

    private int getCurrentYear(String date){
        return Integer.parseInt(date.split("\\.")[2]);
    }

    public void printToast(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
