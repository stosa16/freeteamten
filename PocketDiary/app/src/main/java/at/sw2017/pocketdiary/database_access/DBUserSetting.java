package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void insert(UserSetting userSetting){
        ContentValues content_val = new ContentValues();
        content_val.put("ID", userSetting.getId());
        content_val.put("USERNAME", userSetting.getUserName());
        content_val.put("PICTURE", userSetting.getPicture());
        content_val.put("PIN", userSetting.getPin());
        content_val.put("IS_PIN_ACTIVE", userSetting.isPinActive());
        db.insert("USER_SETTINGS", null, content_val);
    }

    public void insertPin(UserSetting userSetting){
        ContentValues content_val = new ContentValues();
        content_val.put("ID", userSetting.getId());
        content_val.put("PIN", userSetting.getPin());
        content_val.put("IS_PIN_ACTIVE", userSetting.isPinActive());
        db.insert("USER_SETTINGS", null, content_val);
    }

    public UserSetting getUserSetting(int id) {
        Cursor cursor = db.query("USER_SETTINGS", new String[]{"ID", "USERNAME", "PICTURE", "PIN", "IS_PIN_ACTIVE"}, "ID",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        UserSetting userSetting = new UserSetting(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(3)), Boolean.parseBoolean(cursor.getString(4)));
        return userSetting;
    }


    public Cursor getData(){
        Cursor results = db.rawQuery("SELECT * FROM USER_SETTINGS", null);
        return results;
    }
}
