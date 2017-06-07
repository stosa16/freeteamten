package at.sw2017.pocketdiary;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.sw2017.pocketdiary.business_objects.Category;
import at.sw2017.pocketdiary.business_objects.Friend;
import at.sw2017.pocketdiary.business_objects.Statistic;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BOStatisticTest {

    @Test
    public void testStatisticInitWithoutParameters(){
        int id = 1;
        String title = "Title";
        Date date = new Date(2017, 01, 01);
        Integer cat_id = 1;
        Category cat = new Category("Category");
        Integer sub_cat_id = 1;
        Category sub_cat = new Category("Subcategory");;
        String search_term = "Term";
        List<Friend> friend_list = new ArrayList<Friend>();
        friend_list.add(new Friend("Hermann"));

        Statistic statistic = new Statistic();

        statistic.setId(id);
        statistic.setTitle(title);
        statistic.setDateFrom(date);
        statistic.setDateUntil(date);
        statistic.setCategoryId(cat_id);
        statistic.setCategory(cat);
        statistic.setSubCategoryId(sub_cat_id);
        statistic.setSubCategory(sub_cat);
        statistic.setSearchTerm(search_term);
        statistic.setFriendList(friend_list);

        assertEquals(id, statistic.getId());
        assertEquals(title, statistic.getTitle());
        assertEquals(date, statistic.getDateFrom());
        assertEquals(date, statistic.getDateUntil());
        assertEquals(cat_id, statistic.getCategoryId());
        assertEquals(cat, statistic.getCategory());
        assertEquals(sub_cat_id, statistic.getSubCategoryId());
        assertEquals(sub_cat, statistic.getSubCategory());
        assertEquals(search_term, statistic.getSearchTerm());
        assertEquals(friend_list, statistic.getFriendList());
    }

    @Test
    public void testStatisticInitWithAllParameters(){
        int id = 1;
        String title = "Title";
        Date date = new Date(2017, 01, 01);
        Integer cat_id = 1;
        Category cat = new Category("Category");
        Integer sub_cat_id = 1;
        Category sub_cat = new Category("Subcategory");;
        String search_term = "Term";

        Statistic statistic = new Statistic(id, title, date, date, cat_id, cat, sub_cat_id, sub_cat, search_term);

        assertEquals(id, statistic.getId());
        assertEquals(title, statistic.getTitle());
        assertEquals(date, statistic.getDateFrom());
        assertEquals(date, statistic.getDateUntil());
        assertEquals(cat_id, statistic.getCategoryId());
        assertEquals(cat, statistic.getCategory());
        assertEquals(sub_cat_id, statistic.getSubCategoryId());
        assertEquals(sub_cat, statistic.getSubCategory());
        assertEquals(search_term, statistic.getSearchTerm());
    }


}
