package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import at.sw2017.pocketdiary.business_objects.Address;

public class DBAddress extends SQLiteOpenHelper {

    SQLiteDatabase db = this.getWritableDatabase();

    public DBAddress(Context context) {
        super(context, "pocketdiary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insert(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_val = new ContentValues();
        content_val.put("POI", address.getPoi());
        content_val.put("STREET", address.getStreet());
        content_val.put("ZIP", address.getZip());
        content_val.put("CITY", address.getCity());
        content_val.put("COUNTRY", address.getCountry());
        content_val.put("LONGITUDE", address.getLongitude());
        content_val.put("LATITUDE", address.getLatitude());
        long id = db.insert("ADDRESSES", null, content_val);
        db.close();
        return id;
    }

    public long insertLatitudeLongitude(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_val = new ContentValues();
        if (address.getLongitude() != 0) {
            content_val.put("LONGITUDE", address.getLongitude());
        }
        if (address.getLatitude() != 0) {
            content_val.put("LATITUDE", address.getLatitude());
        }
        long id = db.insert("ADDRESSES", null, content_val);
        db.close();
        return id;
    }

    public Address getAddress(int id) {
        String condition = " WHERE ID = " + id;
        String selectQuery = "SELECT * FROM " + "ADDRESSES" + condition;
        Address address = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            address = new Address();
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
            if ((cursor.getString(6) != null && cursor.getString(7) != null) && (cursor.getDouble(6) != 0.0 && cursor.getDouble(7) != 0.0)) {
                address.setLongitude(cursor.getFloat(6));
                address.setLatitude(cursor.getFloat(7));
            }
        }
        db.close();
        return address;
    }
}
