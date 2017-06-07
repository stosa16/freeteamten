package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Picture;

public class DBPicture extends SQLiteOpenHelper{

    public DBPicture(Context context) {
        super(context, "pocketdiary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Picture getPicture(int id) {
        String condition = " WHERE ID = " + id;
        String selectQuery = "SELECT * FROM PICTURES" + condition;
        Picture picture = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            picture = new Picture();
            picture.setId(cursor.getInt(0));
            picture.setFilePath(cursor.getString(1));
            picture.setName(cursor.getString(2));
        }
        db.close();
        return picture;
    }

    public long insert(Picture picture){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_val = new ContentValues();
        content_val.put("FILE_PATH", picture.getFilePath());
        content_val.put("NAME", picture.getName());
        long id = db.insert("PICTURES", null, content_val);
        db.close();
        return id;
    }

    public List<Picture> getPicturesOfEntry(int entry_id) {
        String condition = " WHERE ENTRY_ID = " + entry_id;
        String query_picture_ids = "SELECT * FROM " + "ENTRIES_PICTURES" + condition;
        List<Integer> picture_ids = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query_picture_ids, null);
        if (cursor.moveToFirst()) {
            do {
                picture_ids.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (Integer picture_id : picture_ids) {
            sb.append(prefix);
            prefix = ",";
            sb.append(picture_id);
        }

        List<Picture> pictures = new ArrayList<>();
        String condition_pictures = "WHERE ID IN (" + sb.toString() + ")";
        String query_pictures = "SELECT * FROM PICTURES " + condition_pictures;
        Cursor cursor_pictures = db.rawQuery(query_pictures, null);
        if (cursor_pictures.moveToFirst()) {
            do {
                Picture picture = new Picture();
                picture.setId(cursor_pictures.getInt(0));
                picture.setFilePath(cursor_pictures.getString(1));
                picture.setName(cursor_pictures.getString(2));
                pictures.add(picture);
            } while (cursor_pictures.moveToNext());
        }
        db.close();
        return pictures;
    }

    public Picture getPictureByPath(String file_path) {
        Picture picture = null;
        String condition = "WHERE FILE_PATH = '" + file_path + "'";
        String query_pictures = "SELECT * FROM PICTURES " + condition;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor_pictures = db.rawQuery(query_pictures, null);
        if (cursor_pictures.moveToFirst()) {
            picture = new Picture();
            picture.setId(cursor_pictures.getInt(0));
            picture.setFilePath(cursor_pictures.getString(1));
            picture.setName(cursor_pictures.getString(2));
        }
        db.close();
        return picture;
    }
}
