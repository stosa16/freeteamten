package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import at.sw2017.pocketdiary.business_objects.Address;

public class DBAddress extends SQLiteOpenHelper{

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

    public void insert(Address address){
        ContentValues content_val = new ContentValues();
        content_val.put("ID", address.getId());
        content_val.put("POI", address.getPoi());
        content_val.put("STEET", address.getStreet());
        content_val.put("ZIP", address.getZip());
        content_val.put("CITY", address.getCity());
        content_val.put("COUNTRY", address.getCountry());
        content_val.put("LONGITUDE", address.getLongitude());
        content_val.put("LATITUDE", address.getLatitude());
        db.insert("ADDRESSES", null, content_val);
    }

    public Address getAddress(int id) {
        Cursor cursor = db.query("ADDRESSES", new String[]{"ID", "POI", "STREET", "ZIP", "CITY", "COUNTRY", "LONGITUDE", "LATITUDE"}, "ID",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Address address = new Address(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Float.parseFloat(cursor.getString(6)), Float.parseFloat(cursor.getString(7)));
        return address;
    }

    public Cursor getAllAddresses(){
        Cursor results = db.rawQuery("SELECT * FROM ADDRESSES", null);
        return results;
    }
}
