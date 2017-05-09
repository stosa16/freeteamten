package at.sw2017.pocketdiary;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBEntry;

public final class Helper {

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
    }

    public static Entry getEntryComplete(Context context, int entry_id) {
        DBEntry dbe = new DBEntry(context);
        Entry entry = dbe.getEntry(entry_id);

        DBCategory dbc = new DBCategory(context);
        Category main_category = dbc.getCategory(entry.getMainCategoryId());
        entry.setMainCategory(main_category);
        Category sub_category = dbc.getCategory(entry.getSubCategoryId());
        entry.setSubCategory(sub_category);

        return entry;
    }

    public static void updateBadgeVisibility(TextView text_view, boolean is_visible) {
        if (is_visible) {
            text_view.setVisibility(View.VISIBLE);
        } else {
            text_view.setVisibility(View.GONE);
        }
    }

}
