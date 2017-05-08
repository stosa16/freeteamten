package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.UserSetting;

public class DBUserSetting extends SQLiteOpenHelper{

    SQLiteDatabase db = this.getWritableDatabase();

    public DBUserSetting(Context context) {
        super(context, "pocketdiary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void update(UserSetting userSetting, int id){
        ContentValues content_val = new ContentValues();
        if(!userSetting.getUserName().equals("")) {
            content_val.put("USERNAME", userSetting.getUserName());
        }
        if(userSetting.getPicture() != null){
            //todo
            content_val.put("PICTURE", userSetting.getPicture());
        }
        if(userSetting.getPin() != null) {
            content_val.put("PIN", userSetting.getPin());
        }
        if(userSetting.isPinActive() != null){
            content_val.put("IS_PIN_ACTIVE", userSetting.isPinActive());
        }
        db.update("USER_SETTINGS", content_val, "ID" + "=" + id,null);
    }

    public void insert(UserSetting userSetting){
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_val = new ContentValues();
        content_val.put("USERNAME", userSetting.getUserName());
        content_val.put("PICTURE", userSetting.getPicture());
        content_val.put("PIN", userSetting.getPin());
        content_val.put("IS_PIN_ACTIVE", userSetting.isPinActive());
        db.insert("USER_SETTINGS", null, content_val);
       // db.close();
    }

    public void insertPin(UserSetting userSetting){
        ContentValues content_val = new ContentValues();
        content_val.put("ID", userSetting.getId());
        content_val.put("PIN", userSetting.getPin());
        content_val.put("IS_PIN_ACTIVE", userSetting.isPinActive());
        db.insert("USER_SETTINGS", null, content_val);
    }

    public List<UserSetting> getUserSetting(int id) {
        List<UserSetting> settings_list = new ArrayList<UserSetting>();
        String selectQuery = "SELECT * FROM " + "USER_SETTINGS";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                UserSetting userSetting = new UserSetting();
                userSetting.setId(Integer.parseInt(cursor.getString(0)));
                if(cursor.getString(1) != null){
                    userSetting.setUserName(cursor.getString(1));
                }
                if(cursor.getString(2) != null){
                    //userSetting.setPicture(cursor.getString(2));
                }
                if(cursor.getString(3) != null){
                    userSetting.setPin(cursor.getString(3));
                }
                if(cursor.getString(4) != null){
                    userSetting.setPinActive(cursor.getString(4));
                }

                settings_list.add(userSetting);
            } while (cursor.moveToNext());
        }
        return settings_list;
    }



    public Cursor getData(){
        Cursor results = db.rawQuery("SELECT * FROM USER_SETTINGS", null);
        return results;
    }
}
