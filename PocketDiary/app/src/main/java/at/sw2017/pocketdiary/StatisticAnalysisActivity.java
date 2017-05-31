package at.sw2017.pocketdiary;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBStatisticAnalysis;


/**
 * Created by marku on 30.05.2017.
 */

public class StatisticAnalysisActivity extends Activity {

    private int statistic_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_analysis);
        //getByMainCategory();
        //getBySubCategory();
        getBySearchTerm();
    }

    public void getByMainCategory(){
        DBStatisticAnalysis dbsa = new DBStatisticAnalysis(this);
        List<Entry> entries = dbsa.getEntriesByMainCategoryId(1);
        if(entries.size() > 0) Log.d("Verbose", entries.get(0).getTitle());
    }

    public void getBySubCategory(){
        DBStatisticAnalysis dbsa = new DBStatisticAnalysis(this);
        List<Entry> entries = dbsa.getEntriesBySubCategoryId(5);
        if(entries.size() > 0) Log.d("Verbose", "SUB:" + entries.get(0).getDescription());
    }

    public void getBySearchTerm(){
        DBStatisticAnalysis dbsa = new DBStatisticAnalysis(this);
        List<Entry> entries = dbsa.getEntriesBySearchTerm("term");
        if(entries.size() > 0) {
            for(Entry entry : entries){
                Log.d("Verbose", "SEARCH:" + entry.getTitle());
            }

        }
    }

}
