package at.sw2017.pocketdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Statistic;
import at.sw2017.pocketdiary.database_access.DBStatistic;

/**
 * Created by marku on 25.05.2017.
 */

public class StatisticScreenActivity extends Activity {

    List<Statistic> statistics = new ArrayList<>();
    List<String> strings_statistics = new ArrayList<>();
    ArrayList<Integer> db_ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_screen);
        //insertTestData();
        fillListView();
        Button create_statistic_btn = (Button) findViewById(R.id.statistic_create_btn);
        create_statistic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticScreenActivity.this, CreateStatisticActivity.class);
                startActivity(intent);
            }
        });
    }

    private void insertTestData(){
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        Statistic statistic_1 = new Statistic();
        statistic_1.setTitle("Statistic 2");
        try {
            statistic_1.setDateFrom(date_format.parse("2017-03-26"));
            statistic_1.setDateUntil(date_format.parse("2017-06-26"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        statistic_1.setCategoryId(1);
        statistic_1.setSubCategoryId(1);
        statistic_1.setSearchTerm("This is a searchterm 2");
        DBStatistic db_insert = new DBStatistic(this);
        db_insert.insert(statistic_1);
    }

    private void fillListView(){
        DBStatistic dbStatistic = new DBStatistic(this);
        strings_statistics = new ArrayList<>();
        statistics = dbStatistic.getData();
        ListView statistic_listview = (ListView) findViewById(R.id.statistic_listview);
        final ArrayList<String> lv_statistics = new ArrayList<>();
        for (Statistic statistic : statistics){
            lv_statistics.add(statistic.getTitle());
            db_ids.add(statistic.getId());
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.layout_listview_item, lv_statistics);
        statistic_listview.setAdapter(adapter);
        statistic_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //db_ids.get(position);
                //createAlert(subcategories.get(position));
                printToast(db_ids.get(position).toString());

                Intent i = new Intent(StatisticScreenActivity.this, StatisticAnalysisActivity.class);
                i.putExtra("statistic_id", db_ids.get(position).toString());
                startActivity(i);
            }
        });
    }

    public void printToast(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
