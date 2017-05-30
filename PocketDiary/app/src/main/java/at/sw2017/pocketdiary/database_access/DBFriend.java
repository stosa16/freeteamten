package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void insert(Friend friend){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", friend.getName());
        values.put("IS_DELETED", 0);
        db.insert("FRIENDS", null, values);
        db.close();
    }

    public Friend getFriend(int id) {
        return null;
    }


    public List<Friend> getAllFriends(){
        List<Friend> results = loadStatisticsFromDb("SELECT * FROM FRIENDS");
        return results;
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
