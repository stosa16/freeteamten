package at.sw2017.pocketdiary.database_access;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.business_objects.EntryFriend;
import at.sw2017.pocketdiary.business_objects.Friend;

/**
 * Created by marku on 30.05.2017.
 */

public class DBStatisticAnalysis extends SQLiteOpenHelper {

    private SimpleDateFormat date_fromat = new SimpleDateFormat("yyyy-MM-dd");

    public DBStatisticAnalysis(Context context) {
        super(context, "pocketdiary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Entry> getEntriesByMainCategoryId(int main_cat_id) {
        List<Entry> event_list = getEntryByQuery("SELECT * FROM ENTRIES WHERE MAINCATEGORY_ID="+main_cat_id);
        return event_list;
    }

    public List<Entry> getEntriesBySubCategoryId(int sub_cat_id) {
        List<Entry> event_list = getEntryByQuery("SELECT * FROM ENTRIES WHERE SUBCATEGORY_ID="+sub_cat_id);
        return event_list;
    }

    public List<Entry> getEntriesBySearchTerm(String search_term) {
        List<Entry> event_list = getEntryByQuery("SELECT * FROM ENTRIES WHERE TITLE LIKE '%"+search_term+"%'" +
                "OR DESCRIPTION LIKE '%"+search_term+"%'");
        List<EntryFriend> event_friend_list = getEntryFriendByQuery("SELECT * FROM ENTRIES_FRIENDS");
        for(EntryFriend ef : event_friend_list) {
            int friend_id = ef.getFriendId();
            if(getFriendById(friend_id).getName().contains(search_term)){
                event_list.add(getEntryById(ef.getEventId()));
            }
        }
        return  event_list;
    }

    public Entry getEntryById(int id) {
        Entry entry = new Entry();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ENRIES WHERE ID="+id, null);
        if (cursor.moveToFirst()) {
            do {
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
            } while (cursor.moveToNext());
        }
        db.close();
        return entry;
    }

    public Friend getFriendById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM FRIENDS WHERE ID="+id, null);
        Friend friend = new Friend();
        if (cursor.moveToFirst()) {
            do {
                friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setName(cursor.getString(1));
                if(Integer.parseInt(cursor.getString(2)) > 0) {
                    friend.setDeleted(true);
                }
                else friend.setDeleted(false);

            } while (cursor.moveToNext());
        }
        db.close();
        return friend;
    }

    public List<EntryFriend> getEntryFriendByQuery(String query) {
        List<EntryFriend> entry_friend_list = new ArrayList<EntryFriend>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                EntryFriend entry_friend = new EntryFriend();
                entry_friend.setFriendId(Integer.parseInt(cursor.getString(0)));
                entry_friend.setEventId(Integer.parseInt(cursor.getString(1)));
                entry_friend_list.add(entry_friend);
            } while (cursor.moveToNext());
        }
        db.close();
        return entry_friend_list;
    }

    public List<Entry> getEntryByQuery(String query) {
        List<Entry> entry_list = new ArrayList<Entry>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
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
                entry_list.add(entry);
            } while (cursor.moveToNext());
        }
        db.close();
        return entry_list;
    }
}
