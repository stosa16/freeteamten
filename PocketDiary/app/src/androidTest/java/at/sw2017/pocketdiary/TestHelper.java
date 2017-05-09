package at.sw2017.pocketdiary;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBEntry;

public final class TestHelper {

    public static void initCategories(Context context) {
        DBCategory dbc = new DBCategory(context);
        Category category1 = new Category("Sport");
        Category category2 = new Category("Essen");
        Category category3 = new Category("Party");
        Category subcategory11 = new Category("Walking", 1);
        Category subcategory12 = new Category("Running", 1);
        Category subcategory13 = new Category("Climbing", 1);
        Category subcategory21 = new Category("Chinesisch", 2);
        Category subcategory31 = new Category("Einweihungsfeier", 3);
        Category subcategory32 = new Category("Abschlussfeier", 3);
        dbc.insert(category1);
        dbc.insert(category2);
        dbc.insert(category3);
        dbc.insert(subcategory11);
        dbc.insert(subcategory12);
        dbc.insert(subcategory13);
        dbc.insert(subcategory21);
        dbc.insert(subcategory31);
        dbc.insert(subcategory32);
        dbc.close();
    }

    public static Entry createTestEntryBasic(Context context) {
        DBEntry dbe = new DBEntry(context);
        Entry entry = new Entry();
        entry.setTitle("Test");
        entry.setDescription("This is a test!");
        entry.setMainCategoryId(1);
        entry.setSubCategoryId(4);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 4, 1, 0, 0, 0);
        Date entry_date = calendar.getTime();
        entry.setDate(entry_date);
        long id = dbe.insert(entry);
        dbe.close();
        Entry loaded_entry = Helper.getEntryComplete(context, (int) id);
        return loaded_entry;
    }
}
