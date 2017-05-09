package at.sw2017.pocketdiary.database_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Category;

public class DBCategory extends SQLiteOpenHelper {

    public DBCategory(Context context) {
        super(context, "pocketdiary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", category.getName());
        values.put("PARENT_ID", category.getParentId());
        db.insert("CATEGORIES", null, values);
        db.close();
    }

    public Category getCategory(int id) {
        String condition = " WHERE ID = " + id;
        String selectQuery = "SELECT * FROM " + "CATEGORIES" + condition;
        Category category = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            category = new Category();
            category.setId(cursor.getInt(0));
            category.setName(cursor.getString(1));
            if (cursor.getString(2) != null) {
                category.setParentId(cursor.getInt(2));
            }
            if (cursor.getString(3) != null) {
                category.setDeleted(Boolean.parseBoolean(cursor.getString(3)));
            }
        }
        db.close();
        return category;
    }


    public List<Category> getAllCategories() {
        List<Category> category_list = new ArrayList<Category>();
        String selectQuery = "SELECT * FROM " + "CATEGORIES";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(0)));
                category.setName(cursor.getString(1));
                if(cursor.getString(2) != null){
                    category.setParentId(Integer.parseInt(cursor.getString(2)));
                }
                if(cursor.getString(3) != null) {
                    category.setDeleted(Boolean.valueOf(cursor.getString(3)));
                }
                category_list.add(category);
            } while (cursor.moveToNext());
        }
        db.close();
        return category_list;
    }

    public List<Category> getMainCategories() {
        String condition = " WHERE (IS_DELETED IS NULL OR IS_DELETED = 0) AND PARENT_ID = 0";
        String selectQuery = "SELECT * FROM " + "CATEGORIES" + condition;
        List<Category> categories = loadCategoriesFromDb(selectQuery);
        return categories;
    }

    public List<Category> getSubCategories(int parent_id) {
        String condition = " WHERE (IS_DELETED IS NULL OR IS_DELETED = 0) AND PARENT_ID ='" + parent_id + "'";
        String selectQuery = "SELECT * FROM " + "CATEGORIES" + condition;
        List<Category> categories = loadCategoriesFromDb(selectQuery);
        return categories;
    }

    public List<Category> loadCategoriesFromDb(String query) {
        List<Category> categories = new ArrayList<Category>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(0)));
                category.setName(cursor.getString(1));
                if(cursor.getString(2) != null){
                    category.setParentId(Integer.parseInt(cursor.getString(2)));
                }
                if(cursor.getString(3) != null) {
                    category.setDeleted(Boolean.valueOf(cursor.getString(3)));
                }
                categories.add(category);
            } while (cursor.moveToNext());
        }
        db.close();
        return categories;
    }
}