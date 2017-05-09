package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import at.sw2017.pocketdiary.business_objects.Entry;

public class DBEntry extends SQLiteOpenHelper {

    private SimpleDateFormat date_fromat = new SimpleDateFormat("yyyy-MM-dd");

    public DBEntry(Context context) {
        super(context, "pocketdiary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insert(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TITLE", entry.getTitle());
        values.put("DESCRIPTION", entry.getDescription());
        values.put("MAINCATEGORY_ID", entry.getMainCategoryId());
        values.put("SUBCATEGORY_ID", entry.getSubCategoryId());
        if (entry.getAddress() != null) {
            values.put("ADDRESS_ID", entry.getAddress().getId());
        }
        if (entry.getDate() != null) {
            values.put("DATE", date_fromat.format(entry.getDate()));
        }
        long id = db.insert("ENTRIES", null, values);
        db.close();
        return id;
    }

    public Entry getEntry(int id) {
        String condition = " WHERE ID = " + id;
        String selectQuery = "SELECT * FROM " + "ENTRIES" + condition;
        Entry entry = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            entry = new Entry();
            entry.setId(cursor.getInt(0));
            entry.setTitle(cursor.getString(1));
            entry.setMainCategoryId(cursor.getInt(2));
            entry.setSubCategoryId(cursor.getInt(3));
            if (cursor.getString(4) != null) {
                try {
                    entry.setDate(date_fromat.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (cursor.getString(5) != null) {
                entry.setDescription(cursor.getString(5));
            }
            if (cursor.getString(6) != null) {
                entry.setAddressId(cursor.getInt(6));
            }
        }
        db.close();
        return entry;
    }
}
