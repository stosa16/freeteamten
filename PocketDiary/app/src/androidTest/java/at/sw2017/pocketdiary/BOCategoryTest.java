package at.sw2017.pocketdiary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import at.sw2017.pocketdiary.business_objects.Category;

import static junit.framework.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class BOCategoryTest {

    @Test
    public void testCategoryInitWithOutParameters(){
        String name = "Category";
        int id = 1;
        boolean deleted = false;
        Category category = new Category();
        category.setName(name);
        category.setId(id);
        category.setParentId(id);
        category.setDeleted(deleted);

        assertEquals(name, category.getName());
        assertEquals(id, category.getId());
        assertEquals(id, category.getParentId());
        assertEquals(deleted, category.isDeleted());
    }

    @Test
    public void testCategoryInitWithTwoParameters(){
        String name = "Category";
        int id = 1;
        Category category = new Category(name, id);

        assertEquals(name, category.getName());
        assertEquals(id, category.getParentId());
    }



    @Test
    public void testCategoryInitWithAllParameters(){
        String name = "Category";
        int id = 1;
        boolean deleted = false;
        Category category = new Category(id, name, id, deleted);

        assertEquals(name, category.getName());
        assertEquals(id, category.getId());
        assertEquals(id, category.getParentId());
        assertEquals(deleted, category.isDeleted());
    }
}
