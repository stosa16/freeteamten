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

    public void insert(){
        ContentValues content_val = new ContentValues();
        //content_val.put("DATENBANKSPALTE", parameter);
        //db.insert("TABELLENNAME", null, content_val);
    }

    public UserSetting getUserSetting(int id) {
        return null;
    }


    public Cursor getData(){
        Cursor results = db.rawQuery("SELECT * FROM USER_SETTINGS", null);
        return results;
    }
}
