package at.sw2017.pocketdiary.database_access;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Address;
import at.sw2017.pocketdiary.business_objects.Category;
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
                "OR DESCRIPTION LIKE '%"+search_term+"%'" +
                "OR DATE LIKE '%"+search_term+"%'");

        List<Category> categories_list = getAllCategoriesBySearchTerm(search_term);
        for(Category category : categories_list){
            List<Entry> list_entry_main = getEntriesByMainCategoryId(category.getId());
            List<Entry> list_entry_sub = getEntriesBySubCategoryId(category.getId());

            event_list.addAll(list_entry_main);
            event_list.addAll(list_entry_sub);
        }

        List<Address> addresses_list = getAllAddressesBySearchTerm(search_term);
        for(Address address : addresses_list){
            String query = "SELECT * FROM ENTRIES WHERE ADDRESS_ID="+address.getId();
            List<Entry> list_entry = getEntryByQuery(query);
            event_list.addAll(list_entry);
        }

        List<Entry> friends_list = getAllFriendsBySearchTerm(search_term);
        event_list.addAll(friends_list);

        return  event_list;
    }



    public List<Entry> getEntriesBetweenDates(Date date_start, Date date_end){

        List<Entry> all_entries = getEntryByQuery("SELECT * FROM ENTRIES");
        List<Entry> entries_correct_date = new ArrayList<>();

        for(Entry entry : all_entries){
            if(entry.getDate().compareTo(date_start) >= 0 && entry.getDate().compareTo(date_end) <= 0){
                entries_correct_date.add(entry);
            }
        }

        return  entries_correct_date;
    }

    public List<Entry> getAllFriendsBySearchTerm(String search_term){
        List<Friend> all_friends = new ArrayList<>();

        List<Entry> all_entries = getEntryByQuery("SELECT * FROM ENTRIES");
        List<Entry> valid_entries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM FRIENDS WHERE NAME LIKE '%"+search_term+"%'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                Friend friend = new Friend();
                friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setName(cursor.getString(1));
                friend.setDeleted(Boolean.parseBoolean(cursor.getString(2)));
                all_friends.add(friend);
            }
            while(cursor.moveToNext());
        }

        for(Entry entry : all_entries) {
            String ids = entry.getAllFriends();
            if (ids != null) {
                String[] id_list = ids.split(",");
                for (String id : id_list) {
                    for (Friend friend : all_friends) {
                        if (id.equals(String.valueOf(friend.getId()))) {
                            valid_entries.add(entry);
                            break;
                        }
                    }
                }
            }
        }

        return valid_entries;
    }

    private List<Address> getAllAddressesBySearchTerm(String search_term) {
        List<Address> list_address = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM ADDRESSES " +
                "WHERE POI LIKE '%"+search_term+"%'" +
                "OR STREET LIKE '%"+search_term+"%'" +
                "OR COUNTRY LIKE '%"+search_term+"%'" +
                "OR ZIP LIKE '%"+search_term+"%'" +
                "OR CITY LIKE '%"+search_term+"%'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Address address = new Address();
                address.setId(cursor.getInt(0));
                if (cursor.getString(1) != null) {
                    address.setPoi(cursor.getString(1));
                }
                if (cursor.getString(2) != null) {
                    address.setStreet(cursor.getString(2));
                }
                if (cursor.getString(3) != null) {
                    address.setZip(cursor.getString(3));
                }
                if (cursor.getString(4) != null) {
                    address.setCity(cursor.getString(4));
                }
                if (cursor.getString(5) != null) {
                    address.setCountry(cursor.getString(5));
                }
                if (cursor.getString(6) != null) {
                    address.setLongitude(cursor.getFloat(6));
                }
                if (cursor.getString(7) != null) {
                    address.setLatitude(cursor.getFloat(7));
                }
                list_address.add(address);
            } while (cursor.moveToNext());
        }
        db.close();

        return list_address;
    }

    private List<Category> getAllCategoriesBySearchTerm(String search_term) {
        List<Category> categories_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORIES WHERE NAME LIKE '%"+search_term+"%'", null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(0)));
                category.setName(cursor.getString(1));
                if(Integer.parseInt(cursor.getString(2)) > 0) {
                    category.setParentId(Integer.parseInt(cursor.getString(2)));
                }
                if(cursor.getString(3) != null) {
                    if(Integer.parseInt(cursor.getString(3)) > 0) {
                        category.setDeleted(true);
                    }
                    else category.setDeleted(false);
                }
                categories_list.add(category);
            } while (cursor.moveToNext());
        }
        db.close();

        return categories_list;
    }

   /* private Entry getEntryById(int id) {
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
    }*/

    /*private Friend getFriendById(int id) {
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
    }*/

/*    private List<EntryFriend> getEntryFriendByQuery(String query) {
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
    }*/

    private List<Entry> getEntryByQuery(String query) {
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
                if(cursor.getString(7) != null){
                    entry.setAllFriends(cursor.getString(7));
                }
                entry_list.add(entry);
            } while (cursor.moveToNext());
        }
        db.close();
        return entry_list;
    }
}
