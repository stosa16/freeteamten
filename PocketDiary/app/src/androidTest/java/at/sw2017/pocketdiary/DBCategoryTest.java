package at.sw2017.pocketdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.database_access.DBCategory;
import at.sw2017.pocketdiary.database_access.DBHandler;
import at.sw2017.pocketdiary.database_access.DBStatistic;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DBCategoryTest {

    DBHandler dbh;
    DBCategory dbc;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbh = new DBHandler(context);
        dbc = new DBCategory(context);
    }

    @After
    public void tearDown() {
        dbh.close();
        dbc.close();
    }

    @Test
    public void testInsert() {
        Category category = new Category();
        String cat_name = "Category";
        boolean cat_deleted = false;
        int cat_parent_id = 0;

        category.setName(cat_name);
        category.setDeleted(cat_deleted);
        category.setParentId(cat_parent_id);
        dbc.insert(category);

        List<Category> all_categories = dbc.getAllCategories();
        Category db_cat = all_categories.get(0);

        assertEquals(1, db_cat.getId());
        assertEquals(cat_name, db_cat.getName());
        assertEquals(cat_deleted, db_cat.isDeleted());
        assertEquals(cat_parent_id, db_cat.getParentId());
    }

    @Test
    public void testGetCategoryById() {
        Category category = new Category();
        String cat_name = "Category";
        boolean cat_deleted = false;
        int cat_parent_id = 0;

        category.setName(cat_name);
        category.setDeleted(cat_deleted);
        category.setParentId(cat_parent_id);
        dbc.insert(category);

        Category db_cat = dbc.getCategory(1);

        assertEquals(1, db_cat.getId());
        assertEquals(cat_name, db_cat.getName());
        assertEquals(cat_deleted, db_cat.isDeleted());
        assertEquals(cat_parent_id, db_cat.getParentId());
    }

    @Test
    public void testGetMainCatById() {
        Category category = new Category();
        String cat_name = "Category";
        boolean cat_deleted = false;
        int cat_parent_id = 0;

        category.setName(cat_name);
        category.setDeleted(cat_deleted);
        category.setParentId(cat_parent_id);
        dbc.insert(category);

        Category db_cat = dbc.getMainCategory(1);

        assertEquals(1, db_cat.getId());
        assertEquals(cat_name, db_cat.getName());
        assertEquals(cat_deleted, db_cat.isDeleted());
        assertEquals(cat_parent_id, db_cat.getParentId());
    }

    @Test
    public void testGetMainCatByName() {
        Category category = new Category();
        String cat_name = "Category";
        boolean cat_deleted = false;
        int cat_parent_id = 0;

        category.setName(cat_name);
        category.setDeleted(cat_deleted);
        category.setParentId(cat_parent_id);
        dbc.insert(category);

        Category db_cat = dbc.getMainCategoryByName(cat_name);

        assertEquals(1, db_cat.getId());
        assertEquals(cat_name, db_cat.getName());
        assertEquals(cat_deleted, db_cat.isDeleted());
        assertEquals(cat_parent_id, db_cat.getParentId());
    }

    @Test
    public void testGetAllMainCategories() {
        Category category = new Category();
        String cat_name = "Category";
        boolean cat_deleted = false;
        int cat_parent_id = 0;

        category.setName(cat_name);
        category.setDeleted(cat_deleted);
        category.setParentId(cat_parent_id);
        dbc.insert(category);

        Category db_cat = dbc.getMainCategories().get(0);

        assertEquals(1, db_cat.getId());
        assertEquals(cat_name, db_cat.getName());
        assertEquals(cat_deleted, db_cat.isDeleted());
        assertEquals(cat_parent_id, db_cat.getParentId());
    }

    @Test
    public void testGetSubCatsWithParentId() {
        Category category = new Category();
        String cat_name = "Category";
        boolean cat_deleted = false;
        int cat_parent_id = 0;

        category.setName(cat_name);
        category.setDeleted(cat_deleted);
        category.setParentId(cat_parent_id);
        dbc.insert(category);

        Category sub_category = new Category();
        String sub_cat_name = "Subcategory";
        int sub_cat_parent_id = 1;

        sub_category.setName(sub_cat_name);
        sub_category.setDeleted(cat_deleted);
        sub_category.setParentId(sub_cat_parent_id);
        dbc.insert(sub_category);

        Category db_cat = dbc.getSubCategories(sub_cat_parent_id).get(0);

        assertEquals(2, db_cat.getId());
        assertEquals(sub_cat_name, db_cat.getName());
        assertEquals(cat_deleted, db_cat.isDeleted());
        assertEquals(sub_cat_parent_id, db_cat.getParentId());
    }

    @Test
    public void testUpdate(){
        Category category = new Category();
        String cat_name = "Category";
        boolean cat_deleted = false;
        int cat_parent_id = 0;

        category.setName(cat_name);
        category.setDeleted(cat_deleted);
        category.setParentId(cat_parent_id);
        dbc.insert(category);

        List<Category> all_categories = dbc.getAllCategories();
        Category db_cat = all_categories.get(0);

        assertEquals(1, db_cat.getId());
        assertEquals(cat_name, db_cat.getName());
        assertEquals(cat_deleted, db_cat.isDeleted());
        assertEquals(cat_parent_id, db_cat.getParentId());

        Category updated_cat = new Category();
        String up_cat_name = "Updated_Category";
        updated_cat.setId(1);
        updated_cat.setName(up_cat_name);
        updated_cat.setDeleted(cat_deleted);
        updated_cat.setParentId(cat_parent_id);
        dbc.update(updated_cat);

        db_cat = dbc.getAllCategories().get(0);

        assertEquals(1, db_cat.getId());
        assertEquals(up_cat_name, db_cat.getName());
        assertEquals(cat_deleted, db_cat.isDeleted());
        assertEquals(cat_parent_id, db_cat.getParentId());
    }

    @Test
    public void testDelete() {
        Category category = new Category();
        String cat_name = "Category";
        boolean cat_deleted = false;
        int cat_parent_id = 0;

        category.setName(cat_name);
        category.setDeleted(cat_deleted);
        category.setParentId(cat_parent_id);
        dbc.insert(category);

        List<Category> all_categories = dbc.getAllCategories();
        Category db_cat = all_categories.get(0);

        assertEquals(1, db_cat.getId());
        assertEquals(cat_name, db_cat.getName());
        assertEquals(cat_deleted, db_cat.isDeleted());
        assertEquals(cat_parent_id, db_cat.getParentId());

        dbc.delete(db_cat);

        all_categories = dbc.getMainCategories();
        assertEquals(0, all_categories.size());
    }
}
