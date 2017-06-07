package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.business_objects.UserSetting;

public class DBUserSetting extends SQLiteOpenHelper{

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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_val = new ContentValues();
        if(!userSetting.getUserName().equals("")) {
            content_val.put("USERNAME", userSetting.getUserName());
        }
        if(userSetting.getFilePath() != null){
            content_val.put("FILE_PATH", userSetting.getFilePath());
        }
        if(userSetting.getPicturename() != null){
            content_val.put("NAME", userSetting.getPicturename());
        }
        if(userSetting.getPin() != null) {
            content_val.put("PIN", userSetting.getPin());
        }
        if(userSetting.isPinActive() != null){
            content_val.put("IS_PIN_ACTIVE", userSetting.isPinActive());
        }
        db.update("USER_SETTINGS", content_val, "ID" + "=" + id,null);
        db.close();
    }

    public long insert(UserSetting userSetting){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_val = new ContentValues();
        content_val.put("USERNAME", userSetting.getUserName());
        content_val.put("FILE_PATH", userSetting.getFilePath());
        content_val.put("NAME", userSetting.getPicturename());
        content_val.put("PIN", userSetting.getPin());
        content_val.put("IS_PIN_ACTIVE", userSetting.isPinActive());
        long id = db.insert("USER_SETTINGS", null, content_val);
        db.close();
        return id;
    }

    public long insertPic (Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_val = new ContentValues();
        content_val.put("FILE_PATH", picture.getFilePath());
        content_val.put("NAME", picture.getName());
        long id = db.insert("PICTURES", null, content_val);
        db.close();
        return id;
    }

    public void insertPin(UserSetting userSetting){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_val = new ContentValues();
        content_val.put("ID", userSetting.getId());
        content_val.put("PIN", userSetting.getPin());
        content_val.put("IS_PIN_ACTIVE", userSetting.isPinActive());
        db.insert("USER_SETTINGS", null, content_val);
        db.close();
    }

    public List<UserSetting> getUserSetting(int id) {
        List<UserSetting> settings_list = new ArrayList<UserSetting>();
        String selectQuery = "SELECT * FROM " + "USER_SETTINGS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                UserSetting userSetting = new UserSetting();
                userSetting.setId(Integer.parseInt(cursor.getString(0)));
                //todo maybe index wrong
                if(cursor.getString(1) != null){
                    Log.d("username: ", cursor.getString(1));
                    userSetting.setUserName(cursor.getString(1));
                }
                if(cursor.getString(2) != null){
                    Log.d("path: ", cursor.getString(2));
                    userSetting.setFilePath(cursor.getString(2));
                }

                if(cursor.getString(3) != null){
                    Log.d("picname: ", cursor.getString(3));
                    userSetting.setPicturename(cursor.getString(3));
                }

                if(cursor.getString(4) != null){
                    Log.d("pin: ", cursor.getString(4));
                    userSetting.setPin(cursor.getString(4));
                }
                if(cursor.getString(5) != null){
                    Log.d("pin active: ", cursor.getString(5));
                    userSetting.setPinActive(cursor.getString(5));
                }

                settings_list.add(userSetting);
            } while (cursor.moveToNext());
        }
        db.close();
        return settings_list;
    }



    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM USER_SETTINGS", null);
        return results;
    }
}
