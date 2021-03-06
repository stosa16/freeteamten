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

                Intent i = new Intent(StatisticScreenActivity.this, StatisticAnalysisActivity.class);
                i.putExtra("statistic_id", db_ids.get(position).toString());
                startActivity(i);
            }
        });
    }
}
