package at.sw2017.pocketdiary.database_access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pocketdiary.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table USER_SETTINGS (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, " +
                "PICTURE TEXT, PIN TEXT, IS_PIN_ACTIVE TEXT)");

        db.execSQL("create table ENTRIES (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, " +
                "MAINCATEGORY_ID NUMBER, SUBCATEGORY_ID NUMBER, DATE DATE, DESCRIPTION TEXT, ADDRESS_ID NUMBER)");

        db.execSQL("create table ENTRIES_FRIENDS (FRIEND_ID NUMBER, ENTRY_ID NUMBER)");

        db.execSQL("create table ENTRIES_PICTURES (PICTURE_ID NUMBER, ENTRY_ID NUMBER)");

        db.execSQL("create table PICTURES (ID INTEGER PRIMARY KEY AUTOINCREMENT, PICTURE TEXT)");

        db.execSQL("create table CATEGORIES (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PARENT_ID NUMBER, " +
                "IS_DELETED NUMBER)");

        db.execSQL("create table FRIENDS (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, IS_DELETED TEXT)");

        db.execSQL("create table ADDRESSES (ID INTEGER PRIMARY KEY AUTOINCREMENT, POI TEXT, STREET TEXT, ZIP TEXT, CITY TEXT," +
                "COUNTRY TEXT, LONGITUDE REAL, LATITUDE REAL)");

        db.execSQL("create table STATISTICS (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, DATE_FROM DATE, DATE_UNTIL DATE," +
                "MAINCATEGORY_ID NUMBER, SUBCATEGORY_ID NUMBER, SEARCHTERM TEXT)");

        db.execSQL("create table STATISTICS_FRIENDS (FRIEND_ID NUMBER, STATISTIC_ID NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USER_SETTINGS");
        db.execSQL("DROP TABLE IF EXISTS ENTRIES");
        db.execSQL("DROP TABLE IF EXISTS ENTRIES_FRIENDS");
        db.execSQL("DROP TABLE IF EXISTS ENTRIES_PICTURES");
        db.execSQL("DROP TABLE IF EXISTS PICTURES");
        db.execSQL("DROP TABLE IF EXISTS CATEGORIES");
        db.execSQL("DROP TABLE IF EXISTS FRIENDS");
        db.execSQL("DROP TABLE IF EXISTS ADDRESSES");
        db.execSQL("DROP TABLE IF EXISTS STATISTICS");
        db.execSQL("DROP TABLE IF EXISTS STATISTICS_FRIENDS");
        onCreate(db);
    }

    // Adding new User_Settings
    /*public void addUserSetting(UserSetting userSettings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USERNAME", userSettings.getUserName());
        values.put("IS_PIN_ACTIVE", userSettings.isPinActive());
        // Inserting Row
        db.insert("USER_SETTINGS", null, values);
        db.close(); // Closing database connection
    }*/
}