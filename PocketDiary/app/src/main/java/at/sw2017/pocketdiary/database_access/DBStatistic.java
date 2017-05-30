package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Statistic;

public class DBStatistic extends SQLiteOpenHelper {

    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");

    SQLiteDatabase db = this.getWritableDatabase();

    public DBStatistic(Context context) {
        super(context, "pocketdiary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(Statistic statistic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TITLE", statistic.getTitle());
        if(statistic.getDateFrom() != null) {
            values.put("DATE_FROM", statistic.getDateFrom().toString());
        }
        if(statistic.getDateUntil() != null) {
            values.put("DATE_UNTIL", statistic.getDateUntil().toString());
        }
        if(statistic.getCategoryId() != null) {
            values.put("MAINCATEGORY_ID", statistic.getCategoryId());
        }
        if(statistic.getSubCategoryId() != null) {
            values.put("SUBCATEGORY_ID", statistic.getSubCategoryId());
        }
        if(statistic.getSearchTerm() != null) {
            values.put("SEARCHTERM", statistic.getSearchTerm());
        }
        db.insert("STATISTICS", null, values);
        db.close();
    }

    public void update(Statistic statistic){
        SQLiteDatabase db = this.getWritableDatabase();
        String id = String.valueOf(statistic.getId());
        db.execSQL("UPDATE STATISTICS SET TITLE='"+statistic.getTitle()+"' WHERE id="+id);
        db.execSQL("UPDATE STATISTICS SET DATE_FROM='"+statistic.getDateFrom()+"' WHERE id="+id);
        db.execSQL("UPDATE STATISTICS SET DATE_UNTIL='"+statistic.getDateUntil()+"' WHERE id="+id);
        db.execSQL("UPDATE STATISTICS SET MAINCATEGORY_ID='"+statistic.getCategoryId()+"' WHERE id="+id);
        db.execSQL("UPDATE STATISTICS SET SUBCATEGORY_ID='"+statistic.getSubCategoryId()+"' WHERE id="+id);
        db.execSQL("UPDATE STATISTICS SET SEARCHTERM='"+statistic.getSearchTerm()+"' WHERE id="+id);
        db.close();
    }

    public Statistic getStatistic(int id) {
        List<Statistic> results = loadStatisticsFromDb("SELECT * FROM STATISTICS WHERE ID=" + id);
        Statistic statistic = results.get(0);
        if(statistic.getId() == id){
            return statistic;
        }
        return null;
    }


    public List<Statistic> getData() {
        List<Statistic> results = loadStatisticsFromDb("SELECT * FROM STATISTICS");
        return results;
    }

    public List<Statistic> loadStatisticsFromDb(String query) {
        List<Statistic> statistics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Statistic statistic = new Statistic();
                statistic.setId(Integer.parseInt(cursor.getString(0)));
                statistic.setTitle(cursor.getString(1));
                if(cursor.getString(2) != null){
                    try {
                        statistic.setDateFrom(date_format.parse(cursor.getString(2)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(cursor.getString(3) != null){
                    try {
                        statistic.setDateUntil(date_format.parse(cursor.getString(3)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(cursor.getString(4) != null){
                    statistic.setCategoryId(Integer.parseInt(cursor.getString(4)));
                }
                if(cursor.getString(5) != null){
                    statistic.setSubCategoryId(Integer.parseInt(cursor.getString(5)));
                }
                if(cursor.getString(6) != null){
                    statistic.setSearchTerm(cursor.getString(6));
                }
                statistics.add(statistic);
            } while (cursor.moveToNext());
        }
        db.close();
        return statistics;
    }
}
