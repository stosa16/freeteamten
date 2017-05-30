package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Statistic;

public class DBFriend extends SQLiteOpenHelper{

    SQLiteDatabase db = this.getWritableDatabase();

    public DBFriend(Context context) {
        super(context, "pocketdiary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insert(Friend friend){
        ContentValues content_val = new ContentValues();
        content_val.put("NAME", friend.getName());
        content_val.put("IS_DELETED", friend.isDeleted());
        long id = db.insert("Friends", null, content_val);
        return id;
    }

    public void delete(Friend id){

    }

    public boolean updateFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_val = new ContentValues();
        content_val.put("ID", friend.getId());
        content_val.put("NAME", friend.getName());
        content_val.put("IS_DELETED", friend.isDeleted());
        db.update("Friends", content_val, "ID = ?",new String[] {String.valueOf(friend.getId())});
        return true;
    }

    public Friend getFriend(int id) {
        String condition = " WHERE ID = " + id;
        String selectQuery = "SELECT * FROM " + "FRIENDS" + condition;
        Friend friend = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            friend = new Friend();
            friend.setId(cursor.getInt(0));
            if (cursor.getString(1) != null) {
                friend.setName(cursor.getString(1));
            }
            if (cursor.getString(2) != null) {
                friend.setDeleted(Boolean.parseBoolean(cursor.getString(2)));
            }
        }
        db.close();
        return friend;
    }


    public List<Friend> getAllFriends(){
        List<Friend> results = loadStatisticsFromDb("SELECT * FROM FRIENDS");
        return results;
    }

    public List<Friend> getAllFriends(){
        List<Friend> friend_list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + "FRIENDS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Friend friend = new Friend();
                friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setName(cursor.getString(1));
                friend.setDeleted(Boolean.parseBoolean(cursor.getString(2)));
                friend_list.add(friend);
            }
            while(cursor.moveToNext());
        }
        db.close();
        return friend_list;

    }

    public List<Friend> getAllNondeletedFriends(){
        boolean deleted;
        List<Friend> friend_list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + "FRIENDS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Friend friend = new Friend();
                friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setName(cursor.getString(1));
                friend.setDeleted(Boolean.parseBoolean(cursor.getString(2)));
                deleted = cursor.getString(2).equals("1");
                if(!deleted) {
                    friend_list.add(friend);
                }
            }
            while(cursor.moveToNext());
        }
        db.close();
        return friend_list;

    }
    public List<Friend> loadStatisticsFromDb(String query) {
        List<Friend> friends = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Friend friend = new Friend();
                friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setName(cursor.getString(1));
                boolean is_deleted = Integer.parseInt(cursor.getString(2)) == 1 ? true : false;
                friend.setDeleted(is_deleted);
                friends.add(friend);
            } while (cursor.moveToNext());
        }
        db.close();
        return friends;
    }
}
