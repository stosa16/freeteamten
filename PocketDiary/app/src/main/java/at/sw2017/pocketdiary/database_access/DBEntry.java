package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Picture;

public class DBEntry extends SQLiteOpenHelper{

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

        //todo add in DBHandler: FRIEND TEXT
        //todo maybe LIST instead of TEXT if its possible
        if(entry.getFriends().size() != 0){
            String friend = "";
            for(Friend f:entry.getFriends()){
                friend += f.getId() + ",";
            }
            friend = friend.substring(0, friend.length() - 1);
            //values.put("FRIEND", friend);
            values.put("ALLFRIENDS", friend);
        }
        long id = db.insert("ENTRIES", null, values);
        db.close();
        if (entry.getPictures() != null && entry.getPictures().size() > 0) {
            insertEntryPictures((int) id, entry.getPictures());
        }
        return id;
    }

    public void insertEntryPictures(int entry_id, List<Picture> pictures) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Picture picture : pictures) {
            ContentValues values = new ContentValues();
            values.put("PICTURE_ID", picture.getId());
            values.put("ENTRY_ID", entry_id);
            long id = db.insert("ENTRIES_PICTURES", null, values);
            Log.d("Id", Integer.toString((int) id));
        }
        db.close();
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
            if (cursor.getString(7) != null) {
                entry.setAllFriends(cursor.getString(7));
            }
        }
        db.close();
        return entry;
    }

    public List<Entry> getAllEntries() {
        List<Entry> entry_list = new ArrayList<Entry>();
        String selectQuery = "SELECT * FROM " + "ENTRIES";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry();
                entry.setId(Integer.parseInt(cursor.getString(0)));
                entry.setTitle(cursor.getString(1));
                entry.setMainCategoryId(Integer.parseInt(cursor.getString(2)));
                entry.setSubCategoryId(Integer.parseInt(cursor.getString(3)));
                if (cursor.getString(4) != null) {
                    try {
                        entry.setDate(date_fromat.parse(cursor.getString(4)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(cursor.getString(5) != null){
                    entry.setDescription(cursor.getString(5));
                }
                if(cursor.getString(6) != null){
                    entry.setAddressId(Integer.parseInt(cursor.getString(6)));
                }
                if (cursor.getString(7) != null) {
                    entry.setAllFriends(cursor.getString(7));
                }
                entry_list.add(entry);
            } while (cursor.moveToNext());
        }
        db.close();
        return entry_list;
    }
}
