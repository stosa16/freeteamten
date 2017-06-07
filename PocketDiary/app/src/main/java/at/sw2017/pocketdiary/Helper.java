package at.sw2017.pocketdiary;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import at.sw2017.pocketdiary.business_objects.Address;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Entry;
import at.sw2017.pocketdiary.database_access.DBAddress;
import at.sw2017.pocketdiary.business_objects.Picture;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBEntry;
import at.sw2017.pocketdiary.database_access.DBPicture;

public final class Helper {

    public static void initCategories(Context context) {
        DBCategory dbc = new DBCategory(context);
        Category category1 = new Category("Sports");
        Category category2 = new Category("Food");
        Category category3 = new Category("Entertainment");
        Category category4 = new Category("Others");
        Category subcategory11 = new Category("Walking", 1);
        Category subcategory12 = new Category("Running", 1);
        Category subcategory13 = new Category("Climbing", 1);
        Category subcategory21 = new Category("Chinese", 2);
        Category subcategory31 = new Category("Party", 3);
        Category subcategory32 = new Category("Cinema", 3);
        Category subcategory41 = new Category("Work", 4);
        dbc.insert(category1);
        dbc.insert(category2);
        dbc.insert(category3);
        dbc.insert(category4);
        dbc.insert(subcategory11);
        dbc.insert(subcategory12);
        dbc.insert(subcategory13);
        dbc.insert(subcategory21);
        dbc.insert(subcategory31);
        dbc.insert(subcategory32);
        dbc.insert(subcategory41);
    }

    public static Entry getEntryComplete(Context context, int entry_id) {
        DBEntry dbe = new DBEntry(context);
        Entry entry = dbe.getEntry(entry_id);

        DBCategory dbc = new DBCategory(context);
        Category main_category = dbc.getCategory(entry.getMainCategoryId());
        entry.setMainCategory(main_category);
        Category sub_category = dbc.getCategory(entry.getSubCategoryId());
        entry.setSubCategory(sub_category);

        DBPicture dbp = new DBPicture(context);
        List<Picture> pictures = dbp.getPicturesOfEntry(entry_id);
        entry.setPictures(pictures);

        if (entry.getAddressId() > 0) {
            DBAddress dba = new DBAddress(context);
            Address address = dba.getAddress(entry.getAddressId());
            entry.setAddress(address);
        }

        return entry;
    }

    public static void updateBadgeVisibility(TextView text_view, boolean is_visible) {
        if (is_visible) {
            text_view.setVisibility(View.VISIBLE);
        } else {
            text_view.setVisibility(View.GONE);
        }
    }

    public static Category getCategoryByName(List<Category> categories, String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

}
